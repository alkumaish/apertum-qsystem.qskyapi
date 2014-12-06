package ru.apertum.qsky.model;

import java.util.List;
import javax.naming.NamingException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.apertum.qsky.controller.branch_tree.BranchTreeNode;
import ru.apertum.qsky.ejb.IHibernateEJBLocal;

public class BranchTreeModel {

    public final static String Category = "Category";
    public final static String Contact = "Contact";

    private final BranchTreeNode root;

    private IHibernateEJBLocal hib;

    public BranchTreeModel() {
        try {
            hib = (IHibernateEJBLocal) ((new javax.naming.InitialContext()).lookup("java:comp/env/" + "qskyapi/HibernateEJB"));
        } catch (NamingException ex) {
            throw new RuntimeException("No EJB Hib factory!");
        }

        Session ses = hib.cs();
        ses.beginTransaction();
        List<Branch> list = ses.createCriteria(Branch.class).add(Restrictions.isNull("parent")).list();

        BranchTreeNode[] brs = new BranchTreeNode[list.size()];

        for (int i = 0; i < brs.length; i++) {
            brs[i] = list.get(i).getChildren() != null ? new BranchTreeNode(list.get(i), getChildren(list.get(i))) : new BranchTreeNode(list.get(i));

        }

        root = new BranchTreeNode(null, brs);
        hib.cs().getTransaction().rollback();
    }

    public BranchTreeNode getRoot() {
        return root;
    }

    private BranchTreeNode[] getChildren(Branch parent) {
        System.out.println("---------------------------------------------------------------------------------------- " + parent.getName());
        BranchTreeNode[] brs = new BranchTreeNode[parent.getChildren().size()];

        if (parent.getChildren() == null) {
            return brs;
        } else {
            int i = 0;
            for (Branch br : parent.getChildren()) {
                brs[i++] = br.getChildren() != null ? new BranchTreeNode(br, getChildren(br)) : new BranchTreeNode(br);
            }
        }

        return brs;
    }
}
