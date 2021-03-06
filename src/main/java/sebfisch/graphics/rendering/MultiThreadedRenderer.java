package sebfisch.graphics.rendering;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import sebfisch.graphics.Box;

public class MultiThreadedRenderer extends RendererAdapter<StreamRenderer> {
    public MultiThreadedRenderer() {
        super(new StreamRenderer());
    }

    @Override
    public boolean render(final Box box) {
        // TODO [Task 2.1, Threads] use custom Interruptible instance
        final List<Thread> threads = box.split() //
            .map(part -> (Runnable) () -> renderer.render(part)) //
            .map(Thread::new) //
            .collect(Collectors.toList());
        fork(threads);
        return join(threads);
    }

    private void fork(final List<Thread> threads) {
        threads.forEach(Thread::start);
    }

    // TODO [Task 2.2, Threads] simplify join method
    private boolean join(final List<Thread> threads) {
        while (threads.stream().anyMatch(Thread::isAlive)) {
            if (Thread.currentThread().isInterrupted()) {
                interruptAll(threads);
                return false;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                interruptAll(threads);
                return false;
            }
        }
        return true;
    }

    private void interruptAll(final List<Thread> threads) {
        threads.forEach(Thread::interrupt);
    }
}
