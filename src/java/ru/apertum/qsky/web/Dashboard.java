/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.web;

import java.util.Date;
import javax.naming.NamingException;
import org.hibernate.Session;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.TreeDataEvent;
import ru.apertum.qsky.controller.branch_tree.AdvancedTreeModel;
import ru.apertum.qsky.controller.branch_tree.BranchTreeNode;
import ru.apertum.qsky.ejb.IHibernateEJBLocal;
import ru.apertum.qsky.model.Branch;
import ru.apertum.qsky.model.BranchTreeModel;

/**
 *
 * @author Evgeniy Egorov
 */
public class Dashboard {

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        Selectors.wireEventListeners(view, this);
        doAfterCompose(null);
    }

    private static final long serialVersionUID = 3814570327995355261L;

    @Wire
    private Window dashboardWindow;
    @Wire
    private Tree tree;

    // *** Диалоги изменения состояния
    @Wire("#incBranchPropsDialog #branchPropsDialog")
    Window branchPropsDialog;

    private AdvancedTreeModel contactTreeModel;

    //  @Override
    public void doAfterCompose(Component comp)/* throws Exception*/ {
        // super.doAfterCompose(comp);
        contactTreeModel = new AdvancedTreeModel(new BranchTreeModel().getRoot());

        tree.setItemRenderer(new ContactTreeRenderer());
        tree.setModel(contactTreeModel);
    }

    private IHibernateEJBLocal hib;

    public IHibernateEJBLocal getHib() {
        try {
            if (hib == null) {
                hib = (IHibernateEJBLocal) ((new javax.naming.InitialContext()).lookup("java:comp/env/" + "qskyapi/HibernateEJB"));
            }
        } catch (NamingException ex) {
            throw new RuntimeException("No EJB Hib factory! " + ex);
        }
        return hib;
    }

    /**
     * The structure of tree
     *
     * <pre>
     * &lt;treeitem>
     *   &lt;treerow>
     *     &lt;treecell>...&lt;/treecell>
     *   &lt;/treerow>
     *   &lt;treechildren>
     *     &lt;treeitem>...&lt;/treeitem>
     *   &lt;/treechildren>
     * &lt;/treeitem>
     * </pre>
     */
    private final class ContactTreeRenderer implements TreeitemRenderer<BranchTreeNode> {

        @Override
        public void render(final Treeitem treeItem, BranchTreeNode treeNode, int index) throws Exception {
            final BranchTreeNode ctn = treeNode;
            final Branch branch = (Branch) ctn.getData();
            final Treerow dataRow = new Treerow();
            dataRow.setParent(treeItem);
            treeItem.setValue(ctn);
            treeItem.setOpen(ctn.isOpen());

            final Hlayout hl = new Hlayout();
            hl.appendChild(new Image("/resources/img/label32.png"));
            hl.appendChild(new Label(branch.getName()));
            hl.setSclass("h-inline-block");
            final Treecell treeCell = new Treecell();
            treeCell.appendChild(hl);
            dataRow.setDraggable("true");
            dataRow.appendChild(treeCell);
            dataRow.appendChild(new Treecell("" + branch.getBranchId() + " " + (branch.getActive() ? "Active" : "Disable")));
            dataRow.addEventListener(Events.ON_DOUBLE_CLICK, (Event event) -> {
                final BranchTreeNode clickedNodeValue = (BranchTreeNode) ((Treeitem) event.getTarget().getParent()).getValue();
                final Branch br = clickedNodeValue.getData();
                final Window w = branchPropsDialog;
                w.setTitle(br.toString());
                ((Textbox) w.getFellow("nameBranch")).setText(br.getName());
                ((Intbox) w.getFellow("idBranch")).setValue(br.getBranchId().intValue());
                w.setVisible(true);
                w.setPosition("center");
                w.doModal();
            });

            // Both category row and contact row can be item dropped
            dataRow.setDroppable("true");
            dataRow.addEventListener(Events.ON_DROP, (Event event) -> {
                // The dragged target is a TreeRow belongs to an
                // Treechildren of TreeItem.
                final Treeitem draggedItem = (Treeitem) ((DropEvent) event).getDragged().getParent();
                final BranchTreeNode draggedValue = (BranchTreeNode) draggedItem.getValue();
                //System.out.println("--------------------------START draggedValue " + draggedValue.getData().getId() + " to " + ((BranchTreeNode) treeItem.getValue()).getData().getId());

                if (!draggedValue.isParentOf((BranchTreeNode) treeItem.getValue())) {// не в свою ветку

                    if (draggedValue.getData().getParent() == null
                            || !draggedValue.getData().getParent().getId().equals(((BranchTreeNode) treeItem.getValue()).getData().getId())) {// не в свой уровень

                        contactTreeModel.remove(draggedValue);
                        contactTreeModel.add((BranchTreeNode) treeItem.getValue(), new BranchTreeNode[]{draggedValue});

                        boolean remove = draggedValue.getData().getParent() == null ? true : draggedValue.getData().getParent().getChildren().remove(draggedValue.getData());
                        if (!remove) {
                            throw new RuntimeException("Not delete the child ");
                        }
                        draggedValue.getData().setParent(((BranchTreeNode) treeItem.getValue()).getData());
                        ((BranchTreeNode) treeItem.getValue()).getData().getChildren().add(draggedValue.getData());

                        final Session ses = getHib().cs();
                        ses.beginTransaction();
                        try {
                            ses.saveOrUpdate(((BranchTreeNode) treeItem.getValue()).getData());
                            ses.saveOrUpdate(draggedValue.getData());
                            ses.getTransaction().commit();
                        } catch (Exception ex) {
                            ses.getTransaction().rollback();
                            throw new RuntimeException("Not updated the tree ");
                        }

                    }
                }
            });

        }

    }

    @Command
    public void addBranch() {
        final long l = new Date().getTime() % 10000;
        Branch br = new Branch("New branch " + l);
        br.setBranchId(l);
        final Session ses = getHib().cs();
        ses.beginTransaction();
        try {
            ses.save(br);
            ses.getTransaction().commit();
        } catch (Exception ex) {
            ses.getTransaction().rollback();
            throw new RuntimeException("Not created the new branch " + ex);
        }
        ((BranchTreeNode) tree.getModel().getRoot()).insert(new BranchTreeNode(br), 0);
    }

    @Command
    public void removeBranch() {
        if (tree.getSelectedItem() != null) {
            if (((BranchTreeNode) tree.getSelectedItem().getValue()).getChildCount() != 0) {
                Messagebox.show("У филиала есть подчиненные. Удалите или перенечите их перед удалением.", "Удаление не возможно", Messagebox.OK, Messagebox.ERROR);
                return;
            }
            final Branch br = ((BranchTreeNode) tree.getSelectedItem().getValue()).getData();
            Messagebox.show("Вы уверены что хотите удалить филиал \"" + br + "\"?", "Удаление", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event evt) -> {
                if (evt.getName().equals("onOK")) {

                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ "
                            + ((BranchTreeNode) tree.getSelectedItem().getValue()).getData().getName() + " id=" + ((BranchTreeNode) tree.getSelectedItem().getValue()).getData().getId());

                    if (br.getParent() != null && !br.getParent().getChildren().remove(br)) {
                        throw new RuntimeException("Not delete the child ");
                    }
                    final Session ses = getHib().cs();
                    ses.beginTransaction();
                    try {
                        ses.delete(br);
                        ses.getTransaction().commit();
                    } catch (Exception ex) {
                        ses.getTransaction().rollback();
                        throw new RuntimeException("Not deleted the branch " + ex);
                    }
                    ((BranchTreeNode) tree.getSelectedItem().getValue()).getParent().remove(((BranchTreeNode) tree.getSelectedItem().getValue()));
                }
            });
        }
    }

    @Command
    public void closeBranchPropsDialog() {
        if (tree.getSelectedItem() != null) {
            BranchTreeNode br_node = (BranchTreeNode) tree.getSelectedItem().getValue();
            Branch br = ((BranchTreeNode) tree.getSelectedItem().getValue()).getData();

            br.setName(((Textbox) branchPropsDialog.getFellow("nameBranch")).getText());
            br.setBranchId(((Intbox) branchPropsDialog.getFellow("idBranch")).getValue().longValue());

            final Session ses = getHib().cs();
            ses.beginTransaction();
            try {
                ses.saveOrUpdate(br);
                ses.getTransaction().commit();
            } catch (Exception ex) {
                ses.getTransaction().rollback();
                throw new RuntimeException("Not saved the branch " + ex);
            }

            final int[] ii1 = contactTreeModel.getPath(br_node);
            final int[] ii2 = (new int[ii1.length - 1]);
            System.arraycopy(ii1, 0, ii2, 0, ii2.length);
            contactTreeModel.fireEvent(TreeDataEvent.CONTENTS_CHANGED, ii2, tree.getSelectedItem().getIndex(), tree.getSelectedItem().getIndex());
            branchPropsDialog.setVisible(false);
        }
    }

    @Listen("onSelect = #tree")
    public void selectBranch() { 
        BranchTreeNode selectedNode = (BranchTreeNode) tree.getSelectedItem().getValue();
        System.out.println("SELECTED " + selectedNode); 
        //Category selectedCategory = selectedNode.getData();
        //List<Car> cars = carService.queryByFilter(selectedCategory.getName());
        //resultGrid.setModel(new ListModelList<Car>(cars));
    }

}
