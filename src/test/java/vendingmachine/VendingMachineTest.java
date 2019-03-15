package vendingmachine;

import contract.Bucket;
import contract.Coin;
import contract.Item;
import contract.VendingMachine;
import exceptions.*;
import impl.BucketImpl;
import impl.CoinUtil;
import impl.VendingMachineFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.testng.Assert.*;

public final class VendingMachineTest extends VendingMachineAbstractTest {

    @Test(description = "Vending machine not null after instantiation.")
    public void testVendingMachine(){
        assertNotNull(vm);
    }

    @Test(description = "5 item of each in the machine inventory after initialization.")
    public void testItemInit(){
        vm.getItemInventory().getInventory().forEach((i,l)-> assertEquals((long) l, 5));
    }

    @Test(description = "Buy item with exact price. Change should be empty.")
    public void testBuyItemWithExactPrice() throws SoldOutException, TooMuchInsertedMoneyException,
            ItemNotSelectedException, NotFullyPaidException, NotSufficientChangeException {
        Arrays.stream(Item.values()).forEach(item ->
        {
            try {
                vm.selectItemAndGetPrice(item);
            } catch (SoldOutException e) {
                e.printStackTrace();
            }
            List<Coin> coins = new ArrayList<>(Collections.singletonList(Coin.C10));
            try {
                vm.insertCoin(Coin.C10);
            } catch (TooMuchInsertedMoneyException e) {
                e.printStackTrace();
            }
            while(CoinUtil.getTotal(coins)<(item.getPrice())){
                coins.add(Coin.C10);
                try {
                    vm.insertCoin(Coin.C10);
                } catch (TooMuchInsertedMoneyException e) {
                    e.printStackTrace();
                }
            }
            try {
                assertTrue(vm.collectItemAndChange().getChange().isEmpty());
            } catch (ItemNotSelectedException | NotFullyPaidException | NotSufficientChangeException e) {
                e.printStackTrace();
            }
        });
    }

    @DataProvider(name="itemWithExactPrice")
    public Object[][] itemWithExactPrice(){
        return new Object[][]{
                {Item.COKE, new ArrayList<Coin>(Arrays.asList(Coin.C50,Coin.C20,Coin.C10))},
                {Item.FANTA,new ArrayList<Coin>(Arrays.asList(Coin.C50,Coin.C20,Coin.C10))},
                {Item.JUICE,new ArrayList<Coin>(Collections.singletonList(Coin.C100))},
                {Item.PEPSI,new ArrayList<Coin>(Arrays.asList(Coin.C50,Coin.C20,Coin.C20))},
                {Item.SODA,new ArrayList<Coin>(Collections.singletonList(Coin.C100))},
                {Item.SPRITE,new ArrayList<Coin>(Arrays.asList(Coin.C50,Coin.C20,Coin.C20))},
                {Item.WATER,new ArrayList<Coin>(Collections.singletonList(Coin.C50))},
        };
    }

    @Test(description = "Buy item with exact price. Change should be empty.",
            dataProvider = "itemWithExactPrice")
    public void testBuyItemWithExactPrice(Item item, List<Coin> coins) throws SoldOutException, TooMuchInsertedMoneyException,
            ItemNotSelectedException, NotFullyPaidException, NotSufficientChangeException {
            try {
                vm.selectItemAndGetPrice(item);
            } catch (SoldOutException e) {
                e.printStackTrace();
            }
            coins.forEach(coin-> {
                try {
                    vm.insertCoin(coin);
                } catch (TooMuchInsertedMoneyException e) {
                    e.printStackTrace();
                }
            });
            try {
                assertTrue(vm.collectItemAndChange().getChange().isEmpty());
            } catch (ItemNotSelectedException | NotFullyPaidException | NotSufficientChangeException e) {
                e.printStackTrace();
            }
        }

    @Test(description = "Buy item with more price. Change should not be empty and has expected value. ")
    public void testBuyItemWithMorePrice() throws SoldOutException, ItemNotSelectedException, NotFullyPaidException,
            NotSufficientChangeException, TooMuchInsertedMoneyException {
        Arrays.stream(Item.values()).forEach(item ->
        {
            try {
                vm.selectItemAndGetPrice(item);
            } catch (SoldOutException e) {
                e.printStackTrace();
            }
            List<Coin> coins = new ArrayList<>(Collections.singletonList(Coin.C10));
            try {
                vm.insertCoin(Coin.C10);
            } catch (TooMuchInsertedMoneyException e) {
                e.printStackTrace();
            }
            while(CoinUtil.getTotal(coins)<=(item.getPrice())){
                coins.add(Coin.C10);
                try {
                    vm.insertCoin(Coin.C10);
                } catch (TooMuchInsertedMoneyException e) {
                    e.printStackTrace();
                }
            }
            try {
                assertTrue(vm.collectItemAndChange().getChange().stream().map(Coin::getValue).mapToInt(Integer::intValue).sum()>= 10);
            } catch (ItemNotSelectedException | NotFullyPaidException | NotSufficientChangeException e) {
                e.printStackTrace();
            }
        });
    }

    @DataProvider(name="itemWithMorePrice")
    public Object[][] itemWithMorePrice(){
        return new Object[][]{
                {Item.COKE, new ArrayList<Coin>(Arrays.asList(Coin.C50,Coin.C20,Coin.C20))},
                {Item.FANTA,new ArrayList<Coin>(Arrays.asList(Coin.C50,Coin.C20,Coin.C20))},
                {Item.JUICE,new ArrayList<Coin>(Arrays.asList(Coin.C100,Coin.C10))},
                {Item.PEPSI,new ArrayList<Coin>(Collections.singletonList(Coin.C100))},
                {Item.SODA,new ArrayList<Coin>(Arrays.asList(Coin.C100,Coin.C10))},
                {Item.SPRITE,new ArrayList<Coin>(Collections.singletonList(Coin.C100))},
                {Item.WATER,new ArrayList<Coin>(Arrays.asList(Coin.C50,Coin.C10))},
        };
    }

    @Test(description = "Buy item with more price. Change should not be empty and has expected value. ",
            dataProvider = "itemWithMorePrice")
    public void testBuyItemWithMorePrice(Item item, List<Coin> coins) throws SoldOutException, ItemNotSelectedException, NotFullyPaidException,
            NotSufficientChangeException, TooMuchInsertedMoneyException {
        /*Pour chaque item, il y aura 10 de monnaies en plus par rapport au prix exact*/
        try {
            vm.selectItemAndGetPrice(item);
        } catch (SoldOutException e){
            e.printStackTrace();
        }
        coins.forEach(coin-> {
            try {
                vm.insertCoin(coin);
            } catch (TooMuchInsertedMoneyException e) {
                e.printStackTrace();
            }
        });
        assertEquals(vm.collectItemAndChange().getChange().stream().map(Coin::getValue).mapToInt(Integer::intValue).sum(), 10);
    }

    @Test(description = "Get refund after you have inserted coin and finally dit not buy anything.")
    public void testRefund() throws SoldOutException, TooMuchInsertedMoneyException {
        List<Coin> coins = new ArrayList<>(Arrays.asList(Coin.values()));
        coins.forEach(coin -> {
            try {
                vm.insertCoin(coin);
            } catch (TooMuchInsertedMoneyException e) {
                e.printStackTrace();
            }
        });
        assertEquals(vm.refund(), coins);
    }

    @Test(description = "Factory creates a new vending machine each time.")
    public void testVendingMachineFactory() {
        assertTrue(!vm.equals(VendingMachineFactory.INSTANCE.createVendingMachine()));
    }
}
