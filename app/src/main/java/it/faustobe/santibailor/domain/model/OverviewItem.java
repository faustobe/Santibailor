package it.faustobe.santibailor.domain.model;

/**
 * Modello per gli item della overview (impegni, liste spesa, note)
 */
public class OverviewItem {

    public enum Type {
        IMPEGNO("•"),      // Bullet point per impegni
        LISTA_SPESA("€"),  // Simbolo euro per liste spesa
        NOTA("✎");         // Matita per note

        private final String icon;

        Type(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }
    }

    private final Type type;
    private final String text;
    private final int id;

    public OverviewItem(Type type, String text, int id) {
        this.type = type;
        this.text = text;
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public String getIcon() {
        return type.getIcon();
    }
}
