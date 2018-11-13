package ntu.bustiming.control;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Test
    public void addBusStopToMap() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ntu.bustiming", appContext.getPackageName());
    }
}