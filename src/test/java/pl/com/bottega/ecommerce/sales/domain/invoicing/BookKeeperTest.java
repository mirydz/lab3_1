package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;

public class BookKeeperTest  {

    @Test
    public void ShouldIssueOnePositionInvoiceWhenRequestHasOnePosition() {
        // arrange
        ClientData clientData = new ClientData(Id.generate(), "John Smith");
        Invoice invoice = new Invoice(Id.generate(), clientData);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        ProductData productData = new ProductData(
                Id.generate(),
                new Money(120, "PLN"),
                "Sample Product",
                ProductType.DRUG,
                new Date()
        );

        Money price = new Money(10);
        RequestItem requestItem = new RequestItem(productData, 0, price);
        invoiceRequest.add(requestItem);

        // create mocks
        InvoiceFactory mockInvoiceFactory = Mockito.mock(InvoiceFactory.class);
        Mockito.when(mockInvoiceFactory.create(invoiceRequest.getClientData()))
                .thenReturn(invoice);

        TaxPolicy mockTaxPolicy = Mockito.mock(TaxPolicy.class);
        Mockito.when(mockTaxPolicy.calculateTax(ProductType.DRUG, price))
                .thenReturn(new Tax(new Money(0), ""));

        // act
        BookKeeper bookKeeper = new BookKeeper(mockInvoiceFactory);
        Invoice issuedInvoice = bookKeeper.issuance(invoiceRequest, mockTaxPolicy);

        //assert
        assertThat(issuedInvoice.getItems().size(), is(1));
    }

    @Test
    public void ShouldCalculateTaxTwiceWhenRequestHasTwoPositions() {
        // arrange
        ClientData clientData = new ClientData(Id.generate(), "John Smith");
        Invoice invoice = new Invoice(Id.generate(), clientData);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        ProductData productData = new ProductData(
                Id.generate(),
                new Money(120, "PLN"),
                "Sample Product",
                ProductType.DRUG,
                new Date()
        );

        Money price = new Money(10);
        RequestItem requestItem = new RequestItem(productData, 0, price);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        // create mocks
        InvoiceFactory mockInvoiceFactory = Mockito.mock(InvoiceFactory.class);
        Mockito.when(mockInvoiceFactory.create(invoiceRequest.getClientData()))
                .thenReturn(invoice);

        TaxPolicy mockTaxPolicy = Mockito.mock(TaxPolicy.class);
        Mockito.when(mockTaxPolicy.calculateTax((ProductType) any(), (Money) any()))
                .thenReturn(new Tax(new Money(0), ""));

        // act
        BookKeeper bookKeeper = new BookKeeper(mockInvoiceFactory);
        bookKeeper.issuance(invoiceRequest, mockTaxPolicy);

        //assert
        Mockito.verify(mockTaxPolicy, Mockito.times(2))
                .calculateTax((ProductType) any(), (Money) any());
    }

    @Test
    public void ShouldIssueZeroPositionInvoiceWhenRequestHasNoPositions() {
        // arrange
        ClientData clientData = new ClientData(Id.generate(), "John Smith");
        Invoice invoice = new Invoice(Id.generate(), clientData);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        // create mocks
        InvoiceFactory mockInvoiceFactory = Mockito.mock(InvoiceFactory.class);
        Mockito.when(mockInvoiceFactory.create(invoiceRequest.getClientData()))
                .thenReturn(invoice);

        TaxPolicy mockTaxPolicy = Mockito.mock(TaxPolicy.class);
        Mockito.when(mockTaxPolicy.calculateTax(ProductType.DRUG, new Money(10)))
                .thenReturn(new Tax(new Money(0), ""));

        // act
        BookKeeper bookKeeper = new BookKeeper(mockInvoiceFactory);
        Invoice issuedInvoice = bookKeeper.issuance(invoiceRequest, mockTaxPolicy);

        //assert
        assertThat(issuedInvoice.getItems().size(), is(0));
    }
}