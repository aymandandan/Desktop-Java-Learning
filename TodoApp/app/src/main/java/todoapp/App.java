package todoapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {
    private final ObservableList<Task> tasks = FXCollections.observableArrayList(TaskStorage.loadTasks());

    private void updateFilter(FilteredList<Task> filteredTasks, String query, ToggleGroup group) {
        filteredTasks.setPredicate(task -> {
            boolean matchestext = query == null || query.isEmpty()
                    || task.getDescription().toLowerCase().contains(query.toLowerCase());

            Toggle selected = group.getSelectedToggle();
            if (selected != null) {
                if (((ToggleButton) selected).getText().equals("Active")) {
                    return !task.isCompleted() && matchestext;
                } else if (((ToggleButton) selected).getText().equals("Completed")) {
                    return task.isCompleted() && matchestext;
                }
            }
            return matchestext;
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter a new task...");
        Button addTaskButton = new Button("Add");
        ListView<Task> taskList = new ListView<>();
        FilteredList<Task> filteredTasks = new FilteredList<>(tasks, _ -> true);
        taskList.setItems(filteredTasks);
        TextField searchInput = new TextField();
        searchInput.setPromptText("Search...");

        // filter controls
        ToggleButton allBtn = new ToggleButton("All");
        ToggleButton activeBtn = new ToggleButton("Active");
        ToggleButton completedBtn = new ToggleButton("Completed");

        ToggleGroup group = new ToggleGroup();
        allBtn.setToggleGroup(group);
        activeBtn.setToggleGroup(group);
        completedBtn.setToggleGroup(group);

        allBtn.setSelected(true);

        HBox filterBox = new HBox(10, allBtn, activeBtn, completedBtn);

        // allBtn.selectedProperty().addListener(_ -> filteredTasks.setPredicate(_ -> true));
        // activeBtn.selectedProperty().addListener(_ -> filteredTasks.setPredicate(_1 -> !_1.isCompleted()));
        // completedBtn.selectedProperty().addListener(_ -> filteredTasks.setPredicate(_1 -> _1.isCompleted()));

        // Add tasks
        addTaskButton.setOnAction(_ -> {
            String text = taskInput.getText().trim();
            if (!text.isEmpty()) {
                tasks.add(new Task(text));
                taskInput.clear();
                TaskStorage.saveTasks(tasks);
            }
        });

        // Search
        searchInput.textProperty().addListener(_ -> updateFilter(filteredTasks, searchInput.getText(), group));

        allBtn.setOnAction(_ -> {
            group.selectToggle(allBtn);
            updateFilter(filteredTasks, searchInput.getText(), group);
        });

        activeBtn.setOnAction(_ -> {
            group.selectToggle(activeBtn);
            updateFilter(filteredTasks, searchInput.getText(), group);
        });

        completedBtn.setOnAction(_ -> {
            group.selectToggle(completedBtn);
            updateFilter(filteredTasks, searchInput.getText(), group);
        });

        // cell factory class
        taskList.setCellFactory(_ -> new ListCell<>() {
            private final CheckBox checkbox = new CheckBox();
            private final Label label = new Label();
            private final Button deleteButton = new Button("Delete");
            private final HBox content = new HBox(10, checkbox, label, deleteButton);

            {
                deleteButton.setOnAction(_ -> {
                    tasks.remove(getItem());
                    TaskStorage.saveTasks(tasks);
                });
            }

            {
                checkbox.setOnAction(_ -> {
                    Task task = getItem();
                    if (task != null) {
                        task.setCompleted(checkbox.isSelected());
                        updateLabelStyle(task);
                        TaskStorage.saveTasks(tasks);
                    }
                });
            }

            private void updateLabelStyle(Task task) {
                label.setTextFill(task.isCompleted() ? Color.GRAY : Color.BLACK);
            }

            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setGraphic(null);
                } else {
                    label.setText(task.getDescription());
                    checkbox.setSelected(task.isCompleted());
                    updateLabelStyle(task);
                    setGraphic(content);
                }
            }
        });

        // UI Layout
        HBox inputBox = new HBox(10, taskInput, addTaskButton);
        VBox root = new VBox(10, searchInput, inputBox, filterBox, taskList);
        root.setPrefSize(400, 300);

        // Scene
        primaryStage.setScene(new javafx.scene.Scene(root));
        primaryStage.setTitle("Todo App - v3.0");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
