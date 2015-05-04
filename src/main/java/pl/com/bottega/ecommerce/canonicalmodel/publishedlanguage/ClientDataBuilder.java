package pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage;


public class ClientDataBuilder {
    private Id id;
    private String name;

    public ClientDataBuilder() {
        this.id = Id.generate();
        this.name = "John Smith";
    }

    public ClientDataBuilder withId(Id id) {
        this.id = id;
        return this;
    }

    public ClientDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ClientData build() {
        return new ClientData(this.id, this.name);
    }
}
