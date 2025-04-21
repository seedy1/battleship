module com.sj.batlleship.batlleship {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.sj.batlleship.batlleship to javafx.fxml;
    exports com.sj.batlleship.batlleship;
}