package vendingmachine;

import contract.Bucket;
import contract.Coin;
import contract.Item;
import exceptions.*;
import org.testng.annotations.Test;

import java.util.List;

public class VendingMachineExceptionsTest extends VendingMachineAbstractTest {

        @Test(expectedExceptions = {ItemNotSelectedException.class})
    public void testItemNotSelectedException() throws ItemNotSelectedException,
                NotFullyPaidException, NotSufficientChangeException, SoldOutException, TooMuchInsertedMoneyException {
            Bucket bucket = vm.collectItemAndChange();
    }

    @Test(expectedExceptions = {NotFullyPaidException.class})
    public void testNotFullyPaidException() throws  SoldOutException, TooMuchInsertedMoneyException,
            ItemNotSelectedException, NotSufficientChangeException, NotFullyPaidException {
            vm.selectItemAndGetPrice(Item.COKE);
            Bucket bucket = vm.collectItemAndChange();
    }

    @Test(expectedExceptions = {NotSufficientChangeException.class})
    public void testNotSufficientChangeException() throws SoldOutException, TooMuchInsertedMoneyException,
            ItemNotSelectedException, NotFullyPaidException, NotSufficientChangeException {

    }

    @Test(expectedExceptions = {SoldOutException.class})
    public void testSoldOut() throws SoldOutException, TooMuchInsertedMoneyException, ItemNotSelectedException,
            NotFullyPaidException, NotSufficientChangeException {

    }

    @Test(expectedExceptions = {TooMuchInsertedMoneyException.class})
    public void testTooMuchInsertedMoneyException() throws SoldOutException, ItemNotSelectedException,
            NotSufficientChangeException, TooMuchInsertedMoneyException, NotFullyPaidException {
            for(int i=0;i<10;i++)
                vm.insertCoin(Coin.C100);
        vm.selectItemAndGetPrice(Item.COKE);
        vm.collectItemAndChange();

    }

    @Test(expectedExceptions = {SoldOutException.class})
    public void testReset() throws SoldOutException {

    }

}
