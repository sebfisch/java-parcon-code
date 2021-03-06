package sebfisch.coloring;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EndlessGrid {
    private final int size;
    private ConcurrentHashMap<Integer,ColoredLock> cells;

    public EndlessGrid(final int size) {
        this.size = size;
        cells = new ConcurrentHashMap<>(size*size);
        resetColors();
    }

    public void resetColors() {
        List<Float> hueList = palette().collect(Collectors.toList());
        indices().forEach(index -> {
            cells.compute(index, (i, c) -> new ColoredLock(i, hueList.get(i)));
        });
    }

    public int size() {
        return size;
    }

    public IntStream indices() {
        return IntStream.range(0, size*size);
    }

    public ColoredLock getCell(final int row, final int col) {
        return getCell(index(row, col));
    }

    public ColoredLock getCell(final int index) {
        return cells.get(index);
    }

    public int index(final int row, final int col) {
        final int r = normalize(row);
        final int c = normalize(col);
        return r * size + c;
    }

    private int normalize(final int val) {
        return ((val % size) + size) % size;
    }

    public int row(final int index) {
        return index / size;
    }

    public int col(final int index) {
        return index % size;
    }

    public Stream<Integer> neighborIndices(final int row, final int col) {
        return Stream.of( //
            index(row-1, col),
            index(row, col-1),
            index(row, col+1),
            index(row+1, col)
        );
    }

    public List<ColoredLock> neighbors(final int row, final int col) {
        return neighborIndices(row, col) //
            .map(this::getCell) //
            .collect(Collectors.toList());
    }

    public Set<Float> neighborHues(final int row, final int col) {
        return neighborIndices(row, col) //
            .map(this::getCell) //
            .map(ColoredLock::getHue) //
            .collect(Collectors.toSet());
    }

    public Stream<Float> palette() {
        final int count = size * size;
        final int step = IntStream.iterate(size, i -> i + 1) //
            .filter(i -> gcd(i, count) == 1) //
            .findFirst().orElseThrow();
        return IntStream //
            .iterate(0, i -> i + step) //
            .map(i -> i % count) //
            .mapToObj(i -> (float) i / count) //
            .limit(count);
    }

    private static int gcd(int a, int b) {
        int i;
        while (b != 0) {
            i = b;
            b = a % b;
            a = i;
        }
        return a;
    }
}
