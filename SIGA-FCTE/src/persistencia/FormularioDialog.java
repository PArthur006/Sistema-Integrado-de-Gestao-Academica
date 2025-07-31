package ui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class FormularioDialog extends JDialog {
    private final Map<String, JComponent> campos = new LinkedHashMap<>();
    private boolean okPressionado = false;

    public FormularioDialog(Frame owner, String title, String[] labels) {
        this(owner, title, labels, (String[]) null);
    }

    public FormularioDialog(Frame owner, String title, String[] labels, String[] initialValues) {
        super(owner, title, true);
        JComponent[] components = new JComponent[labels.length];
        for (int i = 0; i < labels.length; i++) {
            components[i] = new JTextField(initialValues != null ? initialValues[i] : "");
        }
        setupUI(owner, labels, components);
    }

    public FormularioDialog(Frame owner, String title, String[] labels, JComponent[] components) {
        super(owner, title, true);
        setupUI(owner, labels, components);
    }

    private void setupUI(Frame owner, String[] labels, JComponent[] components) {
        JPanel painelCampos = new JPanel(new GridLayout(0, 2, 10, 10));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < labels.length; i++) {
            painelCampos.add(new JLabel(labels[i]));
            JComponent component = components[i];
            painelCampos.add(component);
            campos.put(labels[i], component);
        }

        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(e -> {
            okPressionado = true;
            dispose();
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(btnOk);
        painelBotoes.add(btnCancelar);

        setLayout(new BorderLayout());
        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public Map<String, String> getValores() {
        if (!okPressionado) return null;
        Map<String, String> valores = new LinkedHashMap<>();
        campos.forEach((label, component) -> {
            String value = "";
            if (component instanceof JTextField) {
                value = ((JTextField) component).getText().trim();
            } else if (component instanceof JComboBox) {
                Object selected = ((JComboBox<?>) component).getSelectedItem();
                if (selected != null) {
                    value = selected.toString();
                }
            }
            valores.put(label, value);
        });
        return valores;
    }

    public void setCampoEditavel(String label, boolean editavel) {
        JComponent component = campos.get(label);
        if (component instanceof JTextField) {
            ((JTextField) component).setEditable(editavel);
        }
    }
}