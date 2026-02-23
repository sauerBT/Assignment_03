package cs3500.pyramidsolitaire.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Util {
    public static class ListUtil{

        /**
         * Produce a clone of the given collection.
         * @param coll The collection to be cloned.
         * @return The clone of the given collection.
         * @param <K> The type of element within the collection.
         */
        public static <K> List<K> clone(List<K> coll) {
            List<K> result = new ArrayList<>();
            for (K e : coll) {
                result.add(e);
            }
            return result;
        }

        /**
         * Produce the given list with the element at the given position replaced with the given value.
         *
         * @param coll The list to be scrutinized for element replacement.
         * @param position The position of the element to replace.
         * @param value The value to add at the given position
         * @return The list with the replaced value.
         * @param <K> The element type.
         */
        public static <K> List<K> replace(List<K> coll, Integer position, K value) {
            coll.set(0, value);
            return clone(coll);
        }

        public static <K> List<K> getRest(List<K> coll) { return coll.subList(1, coll.size()); }

        /**
         * Produce the first x elements of a given List, with x being a given
         * @param coll The List of elements
         * @param numOfElements The number of x elements to get from the list
         * @return the first x elements of the given list
         * @param <K> The type of element the List uses
         */
        public static <K> List<K> getFirstX(List<K> coll, int numOfElements) {
            return getFirstXHelper(Util.ListUtil.clone(coll), numOfElements, new ArrayList<>());
        }

        private static <K> List<K> getFirstXHelper(List<K> wl, int numOfElements, List<K> listAcc) {
            if (numOfElements == 0) {
                return listAcc;
            } else {
                listAcc.add(wl.getFirst());
                wl.removeFirst();
                return getFirstXHelper(wl, numOfElements - 1, listAcc);
            }
        }

        /**
         * Produce a List with the first x elements removed, with x being a given.
         * Note: If the number of elements requested for removal is greater than the number of elements in
         * the given list, then an empty list is returned.
         *
         * @param coll The List of elements
         * @param numOfElements The number of x elements to remove from the list
         * @return The list with x elements removed
         * @param <K> The type of element the List uses
         */
        public static <K> List<K> removeFirstX(List<K> coll, int numOfElements) {
            return removeFirstXHelper(Util.ListUtil.clone(coll), numOfElements);
        }

        private static <K> List<K> removeFirstXHelper(List<K> listAcc, int numOfElements) {
            if (numOfElements <= 0) {
                return listAcc;
            } else {
                if (!listAcc.isEmpty()) {
                    listAcc.removeFirst();
                }
                return removeFirstXHelper(listAcc, numOfElements - 1);
            }
        }

        public static <K> boolean containsDuplicatesOf(IPred2<K> pred2, List<K> coll, K obj) { return Util.ListUtil.count(pred2, coll, obj) > 1; }

        public static <K> int count(IPred2<K> pred2, List<K> coll, K obj) { return Util.ListUtil.countHelper(pred2, coll, obj, 0); }

        private static <K> int countHelper(IPred2<K> pred2, List<K> coll, K obj, int countAcc) {
            if (coll.isEmpty()) {
                return countAcc;
            } else {
                if (pred2.apply(coll.getFirst(), obj)) {
                    return countHelper(pred2, coll.subList(1, coll.size()), obj, countAcc += 1);
                } else {
                    return countHelper(pred2, coll.subList(1, coll.size()), obj, countAcc);
                }
            }
        }

        public static <K> List<K> add(List<K> coll, K element) {
            List<K> result = Util.ListUtil.copy(coll);
            result.add(element);
            return result;
        }

        public static <K> List<K> append(List<K> coll01, List<K> coll02) {
            List<K> result = Util.ListUtil.copy(coll01);
            result.addAll(coll02);
            return result;
        }

        // TODO
        /**
         * Produce True if the given collection meets the given conditions with respect to the given object.
         *
         * @param pred2 The predicate or conditions.
         * @param coll The given collection.
         * @param obj The given object.
         * @return True if conditions are met, otherwise false.
         * @param <K> The data type of the given collection's elements and the given object.
         */
        public static <K> boolean contains(IPred2<K> pred2, List<K> coll, K obj) {
            if (coll.isEmpty()) {
                return false;
            } else {
                if (pred2.apply(coll.getFirst(), obj)) {
                    return true;
                } else {
                    return Util.ListUtil.contains(pred2, coll.subList(1, coll.size()), obj);
                }
            }
        }

        /**
         * Produce a copy of the given collection.
         *
         * @param coll This is the given collection to be copied.
         * @return The copied collection
         * @param <K> The data object the collection holds
         */
        public static <K> List<K> copy(List<K> coll) { return copyHelper(coll, new ArrayList<>()); }

        private static <K> List<K> copyHelper(List<K> coll, List<K> acc) {
            if (coll.isEmpty()) {
                return acc;
            } else {
                acc.add(coll.getFirst());
                return copyHelper(coll.subList(1, coll.size()), acc);
            }
        }

        /**
         * Abstract function to produce an element from a collection based on a given predicate.
         *
         * @param pred2 The first class function defining the conditions for matching an element.
         * @param coll The given collection
         * @param c A given element to compare individual elements from a collection to.
         * @return The first element from a collection to match the conditions of the given predicate.
         * @param <K> The data type of the element.
         */
        public static <K> Optional<K> findOne(IPred2<K> pred2, List<K> coll, K c) {
            for (K j : coll) {
                if (pred2.apply(j, c)) { return Optional.of(j); }
            }
            return Optional.empty();
        }

        /**
         * Compare all elements of a given comparison List to all elements of a given collection using the given comparison function
         * and return all elements from the collection that meet the comparison predicate.
         *
         * @param pred2 The comparison function.
         * @param coll The given collection.
         * @param comp The given comparison list.
         * @return The list of elements that match the given comparison predicate.
         * @param <K> The type of array elements.
         */
        public static <K> List<K> findAll(IPred2<K> pred2, List<K> coll, List<K> comp) {
            return findAllHelper(pred2, coll, comp, new ArrayList<>());
        }

        private static <K> List<K> findAllHelper(IPred2<K> pred2, List<K> coll, List<K> comp, List<K> acc) {
            if (coll.isEmpty()) {
                return acc;
            } else {
                Optional<K> cond = Util.ListUtil.findOne(pred2, comp, coll.getFirst());
                if (cond.isPresent()) {
                    acc.add(coll.getFirst()); // MUTATION
                }
                return findAllHelper(pred2, coll.subList(1, coll.size()), comp, acc);
            }
        }

        // TODO
        /**
         * Produce a new List that contains only elements from the original list that meet the conditions defined by the given
         * predicate.  A List is given for comparison elements. The predicate comparison is compares every element of the
         * given collection to every element of the comparison collection.
         *
         * @param pred2 The first class function for providing conditions.
         * @param coll The given collection.
         * @param compColl The list of elements to compare.
         * @return A list of elements including only those from the original given list that pass the predicate conditions.
         * @param <K> The data type of the elements.
         */
        public static <K> List<K> findIfExclude(IPred2<K> pred2, List<K> coll, List<K> compColl) {
            return findIfExcludeHelper(pred2, coll, compColl, new ArrayList<>());
        }

        private static <K> List<K> findIfExcludeHelper(IPred2<K> pred2, List<K> coll, List<K> compColl, List<K> acc) {
            if (coll.isEmpty()) {
                return acc;
            } else if (compColl.isEmpty()) {
                return coll;
            } else {
                if (!Util.ListUtil.contains(pred2, compColl, coll.getFirst())) {
                    acc.add(coll.getFirst());
                }
                return findIfExcludeHelper(pred2, coll.subList(1, coll.size()), compColl, acc);
            }
        }

        public static <K, R> List<R> map(Function<K, R> func, List<K> lok) {
            if (lok.isEmpty()) {
                return new ArrayList<>();
            } else {
                List<R> result = new ArrayList<>();
                for (K k : lok) {
                    result.add(func.apply(k));
                }
                return result;
            }
        }

        public static <K, R> R foldr(BiFunction<K, R, R> func, List<K> lok, R r) {
            if (lok.isEmpty()) {
                return r;
            } else {
                R result = r;
                for (K k: lok.reversed()) {
                    result = func.apply(k, result);
                }
                return result;
            }
        }

        public static <K, R> R foldl(BiFunction<K, R, R> func, List<K> lok, R r) {
            if (lok.isEmpty()) {
                return r;
            } else {
                R result = r;
                for (K k: lok) {
                    result = func.apply(k, result);
                }
                return result;
            }
        }

        /**
         * Produce the abstract function for filtering out particular given values based on a given predicate.
         *
         * @param pred Predicate function.
         * @param lok Given list of elements
         * @return Filtered list of elements
         * @param <K> Type of element.
         */
        public static <K> List<K> filter(Predicate<K> pred, List<K> lok) {
            return Util.ListUtil.filterHelper(pred, lok, new ArrayList<>());
        }

        private static <K> List<K> filterHelper(Predicate<K> pred, List<K> lok, List<K> acc) {
            for (K k : lok) {
                if (pred.test(k)) {
                    acc.add(k);
                }
            }
            return acc;
        }
    }

    public static class GameUtil{
        /**
         * Produce a list of cards from a given list of IPairs.
         *
         * @param lop list of IPair
         * @return the list of Card
         */
        public static <K> List<K> extractCard(List<IPair<K>> lop) {
            return Util.ListUtil.foldl(new IPairToCard<>(), lop, new ArrayList<>());
        }

        /**
         * Produce the total sum of the given card values
         *
         * @param loc the given list of cards
         * @return the sum
         */
        public static int getCardTotalValue(List<Card> loc) {
            return Util.ListUtil.foldl(new SumCardValues(), loc, 0);
        }

        // TODO -- simplify
        /**
         * Produce true if there is a move that a player can make.
         * <p>
         *    What constitutes a move is at least one set of two cards whose combined values equal 13 or
         *    a single card whose value is 13.
         * </p>
         *
         * @param lodc List of draw cards
         * @param lopc List of uncovered cards
         * @return True when there is a move to be played, false otherwise.
         */
        public static boolean isMove(List<Card> lodc, List<Card> lopc) {
            return !(Util.ListUtil.filter(new IsThirteen(), lopc).isEmpty() && // <-- check for individual removes
                    Util.ListUtil.findAll(new AddsToThirteen(), lopc, lopc).isEmpty() && // <-- check for two card removals
                    Util.ListUtil.findAll(new AddsToThirteen(), lopc, lodc).isEmpty()); // <-- check for draw card removals

        }

        // TODO
        /**
         * Produce a row where the empty positions are filled with "Empty" Pairs.
         * @param rowNum The given row number of the given List of Pair
         * @param lop The given list of pair
         * @return A list of pair with all empty positions represented as pairs with empty elements.
         * @param <K> The type of element in a pair.
         */
        public static <K> List<IPair<K>> fillEmptyPositions(int rowNum, List<IPair<K>> lop) {
            return fillEmptyPositionsHelper(rowNum, lop, 0, new ArrayList<>());
        }

        private static <K> List<IPair<K>> fillEmptyPositionsHelper(int rowNum, List<IPair<K>> lopAcc, int positionAcc, List<IPair<K>> acc) {
            if (rowNum < positionAcc) { // Reached the max number of positions
                return acc;
            } else if (lopAcc.isEmpty()) { // There are no more pairs in the given list of pair
                acc.add(IPair.empty(positionAcc, rowNum));
                return fillEmptyPositionsHelper(rowNum, lopAcc, positionAcc + 1, acc);
            } else {
                if (lopAcc.getFirst().position().equals(positionAcc)) { // Current position already exists in the given list of pair
                    acc.add(lopAcc.getFirst()); // MUTATION: Add the first pair from the lopAcc to the result acc.
                    lopAcc.removeFirst(); // MUTATION: Remove first pair from the lopAcc.
                } else { // Current position does not exists in the given list of pair
                    acc.add(IPair.empty(positionAcc, rowNum));
                }
                return fillEmptyPositionsHelper(rowNum, lopAcc, positionAcc + 1, acc);
            }
        }
    }
    /**
     * Produce the calculated sum of all integers adding up to the given integer.
     *
     * @param n The given integer value
     * @return The resulting sum value.
     * @throws IllegalArgumentException The given integer cannot be negative.
     */
    public static int sumUp(int n) {
        if (n < 0) { throw new IllegalArgumentException("Given integer cannot be less than zero."); }
        else if (n == 0) { return 0; }
        else {
            return n + sumUp(n - 1);
        }
    }
}

class IPairToCard<K> implements BiFunction<IPair<K>, List<K>, List<K>> {
    public List<K> apply(IPair<K> pair, List<K> acc) {
        if (pair.element().isPresent()) {
            acc.add(pair.element().get());
        }
        return acc;
    }
}

/**
 * The function class to produce the sum of Card elements within from a list of Card.
 */
class SumCardValues implements BiFunction<Card, Integer, Integer> {
    public Integer apply(Card c, Integer acc) { return acc + c.getValue(); }
}

/**
 * The predicate function class that compares two cards and determines if their combined value is 13.
 */
class AddsToThirteen implements IPred2<Card> {
    public boolean apply(Card arg1, Card arg2) { return (arg1.getValue() + arg2.getValue()) == 13; }
}

class IsThirteen implements Predicate<Card> {
    public boolean test(Card arg) { return arg.getValue() == 13; }
}