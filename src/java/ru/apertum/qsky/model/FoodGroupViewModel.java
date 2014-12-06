/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.zkoss.zul.GroupComparator;
import org.zkoss.zul.GroupsModelArray;

/**
 *
 * @author Evgeniy Egorov
 */
public class FoodGroupViewModel {

    private boolean showGroup = true;

    public FoodGroupingModel getFoodModel() {
        return new FoodGroupingModel(FoodData.getAllFoods(), new FoodComparator(), this.showGroup);
    }

    public static class FoodGroupingModel extends GroupsModelArray<Food, String, String, Object> {

        private static final long serialVersionUID = 1L;

        private static final String footerString = "Total %d items";

        private boolean showGroup;

        public FoodGroupingModel(List<Food> data, Comparator<Food> cmpr, boolean showGroup) {
            super(data.toArray(new Food[0]), cmpr);

            this.showGroup = showGroup;
        }

        @Override
        protected String createGroupHead(Food[] groupdata, int index, int col) {
            String ret = "";
            if (groupdata.length > 0) {
                ret = groupdata[0].getCategory();
            }

            return ret;
        }

        @Override
        protected String createGroupFoot(Food[] groupdata, int index, int col) {
            return String.format(footerString, groupdata.length);
        }

        @Override
        public boolean hasGroupfoot(int groupIndex) {
            boolean retBool = false;

            if (showGroup) {
                retBool = super.hasGroupfoot(groupIndex);
            }

            return retBool;
        }
    }

    private static class FoodData {

        private static final ArrayList<Food> foods = new ArrayList<>();

        static {
            foods.add(new Food("Vegetables", "Asparagus", "Vitamin K", 115, 43, "1 cup - 92 grams"));
            foods.add(new Food("Vegetables", "Beets", "Folate", 33, 74, "1 cup - 170 grams"));
            foods.add(new Food("Vegetables", "Bell peppers", "Vitamin C", 291, 24, "1 cup - 92 grams"));
            foods.add(new Food("Vegetables", "Cauliflower", "Vitamin C", 92, 28, "1 cup - 124 grams"));
            foods.add(new Food("Vegetables", "Eggplant", "Dietary Fiber", 10, 27, "1 cup - 99 grams"));
            foods.add(new Food("Vegetables", "Onions", "Chromium", 21, 60, "1 cup - 160 grams"));
            foods.add(new Food("Vegetables", "Potatoes", "Vitamin C", 26, 132, "1 cup - 122 grams"));
            foods.add(new Food("Vegetables", "Spinach", "Vitamin K", 1110, 41, "1 cup - 180 grams"));
            foods.add(new Food("Vegetables", "Tomatoes", "Vitamin C", 57, 37, "1 cup - 180 grams"));
            foods.add(new Food("Seafood", "Salmon", "Tryptophan", 103, 261, "4 oz - 113.4 grams"));
            foods.add(new Food("Seafood", "Shrimp", "Tryptophan", 103, 112, "4 oz - 113.4 grams"));
            foods.add(new Food("Seafood", "Scallops", "Tryptophan", 81, 151, "4 oz - 113.4 grams"));
            foods.add(new Food("Seafood", "Cod", "Tryptophan", 90, 119, "4 oz - 113.4 grams"));
            foods.add(new Food("Fruits", "Apples", "Manganese", 33, 61, "1 cup - 160 grams"));
            foods.add(new Food("Fruits", "Cantaloupe", "Vitamin C", 112, 56, "1 cup - 160 grams"));
            foods.add(new Food("Fruits", "Grapes", "Manganese", 33, 61, "1 cup - 92 grams"));
            foods.add(new Food("Fruits", "Pineapple", "Manganese", 128, 75, "1 cup - 155 grams"));
            foods.add(new Food("Fruits", "Strawberries", "Vitamin C", 24, 48, "1 cup - 150 grams"));
            foods.add(new Food("Fruits", "Watermelon", "Vitamin C", 24, 48, "1 cup - 152 grams"));
            foods.add(new Food("Poultry & Lean Meats", "Beef, lean organic", "Tryptophan", 112, 240, "4 oz - 113.4 grams"));
            foods.add(new Food("Poultry & Lean Meats", "Lamb", "Tryptophan", 109, 229, "4 oz - 113.4 grams"));
            foods.add(new Food("Poultry & Lean Meats", "Chicken", "Tryptophan", 121, 223, "4 oz - 113.4 grams"));
            foods.add(new Food("Poultry & Lean Meats", "Venison ", "Protein", 69, 179, "4 oz - 113.4 grams"));
            foods.add(new Food("Grains", "Corn ", "Vatamin B1", 24, 177, "1 cup - 164 grams"));
            foods.add(new Food("Grains", "Oats ", "Manganese", 69, 147, "1 cup - 234 grams"));
            foods.add(new Food("Grains", "Barley ", "Dietary Fiber", 54, 270, "1 cup - 200 grams"));
        }

        public static ArrayList<Food> getAllFoods() {
            return new ArrayList<>(foods);
        }

        public static Food[] getAllFoodsArray() {
            return foods.toArray(new Food[foods.size()]);
        }

        // This Method only used in "Data Filter" Demo
        public static ArrayList<Food> getFilterFoods(FoodFilter foodFilter) {
            ArrayList<Food> somefoods = new ArrayList<>();
            String cat = foodFilter.getCategory().toLowerCase();
            String nm = foodFilter.getName().toLowerCase();
            String nut = foodFilter.getNutrients().toLowerCase();

            foods.stream().filter((tmp) -> (tmp.getCategory().toLowerCase().contains(cat)
                    && tmp.getName().toLowerCase().contains(nm)
                    && tmp.getTopNutrients().toLowerCase().contains(nut))).forEach((tmp) -> {
                        somefoods.add(tmp);
                    });
            return somefoods;
        }

        // This Method only used in "Header and footer" Demo
        public static List<Food> getFoodsByCategory(String category) {
            ArrayList<Food> somefoods = new ArrayList<>();
            foods.stream().filter((tmp) -> (tmp.getCategory().equalsIgnoreCase(category))).forEach((tmp) -> {
                somefoods.add(tmp);
            });
            return somefoods;
        }
    }

    private static class FoodComparator implements Comparator<Food>, GroupComparator<Food>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(Food o1, Food o2) {
            return o1.getCategory().compareTo(o2.getCategory());
        }

        @Override
        public int compareGroup(Food o1, Food o2) {
            if (o1.getCategory().equals(o2.getCategory())) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public static class Food {

        private String category;
        private String name;
        private String topNutrients;
        private Integer dailyPercent;
        private Integer calories;
        private String quantity;

        public Food(String category, String name, String topNutrients,
                Integer dailyPercent, Integer calories, String quantity) {
            this.category = category;
            this.name = name;
            this.topNutrients = topNutrients;
            this.dailyPercent = dailyPercent;
            this.calories = calories;
            this.quantity = quantity;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTopNutrients() {
            return topNutrients;
        }

        public void setTopNutrients(String topNutrients) {
            this.topNutrients = topNutrients;
        }

        public Integer getDailyPercent() {
            return dailyPercent;
        }

        public void setDailyPercent(Integer dailyPercent) {
            this.dailyPercent = dailyPercent;
        }

        public Integer getCalories() {
            return calories;
        }

        public void setCalories(Integer calories) {
            this.calories = calories;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

    }

    private static class FoodFilter {

        public FoodFilter() {
        }

        private String getCategory() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private String getName() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private String getNutrients() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

}
