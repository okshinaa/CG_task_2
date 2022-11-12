module ru.vsu.cs.okshina_v_a.kg_task_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ru.vsu.cs.okshina_v_a.kg_task_2 to javafx.fxml;
    exports ru.vsu.cs.okshina_v_a.kg_task_2;
}