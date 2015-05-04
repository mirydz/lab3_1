package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

import java.util.List;

public class InvoiceBuilder {
    private ClientData client = new ClientDataBuilder().build();
    private Id id = Id.generate();

    public InvoiceBuilder() {}

    public InvoiceBuilder(Invoice invoice) {
        this.client = invoice.getClient();
    }

    public InvoiceBuilder withClient(ClientData client) {
        this.client = client;
        return this;
    }

    public InvoiceBuilder withId(Id id) {
        this.id = id;
        return this;
    }

    public Invoice build() {
        return new Invoice(this.id, this.client);
    }
}
