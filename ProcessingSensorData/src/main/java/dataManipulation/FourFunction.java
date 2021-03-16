package dataManipulation;

@FunctionalInterface
public interface FourFunction<One, Two, Three, Four> {
    public Four apply(One one, Two two, Three three);
}