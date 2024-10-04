package it.faustobe.santibailor.domain.model;

public class SearchResult implements Searchable {
    private int id;
    private String type;
    private String content;

    public SearchResult(int id, String type, String content) {
        this.id = id;
        this.type = type;
        this.content = content;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getSearchableContent() {
        return content;
    }

    @Override
    public String getType() {
        return type;
    }

    // Getter e setter...
}
