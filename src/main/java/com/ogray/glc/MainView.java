package com.ogray.glc;


import com.ogray.glc.math.Res;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;


/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route
@PWA(name = "glc",
        shortName = "glc",
        description = "Gravitational Lensing Computing",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@Slf4j
public class MainView extends VerticalLayout implements HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>> {
    ComboBox<String> comboBoxSourceType = new ComboBox<>("Source");
    ComboBox<String> comboBoxCalcMode = new ComboBox<>("Calculation mode");

    Manager boss = new Manager(null);
    Image image = null;

    TextField sourceSizeField = new TextField("Source size (RE)");
    TextField sizeREField = new TextField("Size (RE)");

    TextField sigmaCField = new TextField("SigmaC");
    TextField gammaField = new TextField("Gamma");

    TextField ngField = new TextField("NG");
    TextField m0Field = new TextField("Star mass");

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public MainView(@Autowired GreetService service) {
        initComponents();

        boss.init();
        boss.setParams(Persist.getInstance());
        boss.refreshGravs();
        Res res = boss.render();

        final byte[] jpegData = boss.map.field.getJPG();
        StreamResource resource = new StreamResource("image.jpg", () ->
                new ByteArrayInputStream(jpegData));
        image = new Image(resource, "image");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Render",
                e -> {
            dataFromUItoPersist();
            UI.getCurrent().getPage().reload();
        });

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button has a more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        dataFromPersistToUI();

        image.setAlt("Rendering time: "+res.t+"ms");
        image.setTitle("gravitational lensing");
        add(image);
        add(button);

       // Label renderTime = new Label("Rendering time: "+res.t+"ms");
       // renderTime.setHeight("10");
       // add(renderTime);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        HorizontalLayout layout = new HorizontalLayout(comboBoxSourceType, sourceSizeField, sizeREField);
        layout.setDefaultVerticalComponentAlignment(Alignment.END);
        add(layout);

        HorizontalLayout layout2 = new HorizontalLayout(comboBoxCalcMode, sigmaCField, gammaField);
        layout2.setDefaultVerticalComponentAlignment(Alignment.END);
        add(layout2);

        HorizontalLayout layout3 = new HorizontalLayout(ngField, m0Field);
        layout3.setDefaultVerticalComponentAlignment(Alignment.END);
        add(layout3);

    }

    void dataFromPersistToUI() {
        sourceSizeField.setValue(""+Persist.getInstance().getSourceSize());
        sizeREField.setValue("" + Persist.getInstance().getSizeRE());
        sigmaCField.setValue(""+Persist.getInstance().getSigmaC());
        gammaField.setValue(""+Persist.getInstance().getGamma());
        ngField.setValue("" + Persist.getInstance().getNg());
        m0Field.setValue("" + Persist.getInstance().getStarM0());
    }
    /**
     * load data from UI to Persist object
     */
    private void dataFromUItoPersist() {
        try {
            float value = Float.parseFloat(sourceSizeField.getValue());
            Persist.getInstance().setSourceSize(value);

            value = Float.parseFloat(sizeREField.getValue());
            Persist.getInstance().setSizeRE(value);

            value = Float.parseFloat(sigmaCField.getValue());
            Persist.getInstance().setSigmaC(value);

            value = Float.parseFloat(gammaField.getValue());
            Persist.getInstance().setGamma(value);

            int valueInt = Integer.parseInt(ngField.getValue());
            Persist.getInstance().setNg(valueInt);

            value = Float.parseFloat(m0Field.getValue());
            Persist.getInstance().setStarM0 (value);

        } catch(NumberFormatException ex) {}

        switch( comboBoxCalcMode.getValue() ) {
            case "FFC":
                Persist.getInstance().setCalcMode(0);
                break;
            case "HFC":
                Persist.getInstance().setCalcMode(1);
                break;
            case "SSD":
                Persist.getInstance().setCalcMode(2);
                break;
            case "ONEG":
                Persist.getInstance().setCalcMode(3);
                break;
            case "WITT":
                Persist.getInstance().setCalcMode(4);
                break;
        }
    }

    /*byte[] generateSourceImage() {
        if(this.comboBoxSourceType==null || this.comboBoxSourceType.getValue()==null) {
            source = new GausSource(sourceSize, QSize);
        } else
            switch(  this.comboBoxSourceType.getValue()) {
                case FlatSource.NAME:
                    source = new FlatSource(sourceSize, QSize);
                    break;
                case GausSource.NAME:
                    source = new GausSource(sourceSize, QSize);
                    break;
                default:
                    source = new FlatSource(sourceSize, QSize);

            }
       // source.generate();

        byte[][] raw = source.getData();

        int[][] rgb = Utils.makeGreyRGB(raw, sourceSize, sourceSize);
        byte[] jpg = null;
        try {
            jpg = Utils.rawToJpeg(rgb, sourceSize, sourceSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jpg;
    }*/

    void initComponents() {
        String []sourceTypes = new String[5];
        sourceTypes[0] = "Flat";
        sourceTypes[1] = "Gaussian";
        sourceTypes[2] ="Exponent";
        sourceTypes[3] ="Limb";
        sourceTypes[4] = "A-Disk";
        comboBoxSourceType.setItems(sourceTypes);
        comboBoxSourceType.setValue(sourceTypes[Persist.getInstance().getSourceType()]);
        comboBoxSourceType.addValueChangeListener(this);

        String [] calcModes = new String[5];
        calcModes[0] = "FFC";
        calcModes[1] ="HFC";
        calcModes[2] ="SSD";
        calcModes[3] = "ONEG";
        calcModes[4] = "WITT";

        comboBoxCalcMode.setItems(calcModes);
        comboBoxCalcMode.setValue(calcModes[Persist.getInstance().getCalcMode ()]);

    }

    @Override
    public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
        log.info("valueChanged " + this.comboBoxSourceType.getValue());
        switch (comboBoxSourceType.getValue()) {
            case "Flat":
                Persist.getInstance().setSourceType(0);
                break;
            case "Gaussian":
                Persist.getInstance().setSourceType(1);
                break;
            case "Exponent":
                Persist.getInstance().setSourceType(2);
                break;
            case "Limb":
                Persist.getInstance().setSourceType(3);
                break;
            case "A-Disk":
                Persist.getInstance().setSourceType(4);
                break;
        }
    }
}
