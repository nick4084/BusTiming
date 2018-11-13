package ntu.bustiming.control;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LTADatamallControllerTest {
    LTADatamallController lta;
    int test;
    @Before
    public void setUp(){
        try{
        Context context = InstrumentationRegistry.getTargetContext();
        lta = new LTADatamallController(context);
        test = (lta.getBusStops(10)).getJSONArray("value").length();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void singleBusArrivalTiming() {
        try {
            //valid
            assertFalse(0 == (lta.SingleBusArrivalTiming("16", "92181")).getJSONArray("Services").length());
            assertFalse(0 == (lta.SingleBusArrivalTiming("965", "58301")).getJSONArray("Services").length());
            assertFalse(0 == (lta.SingleBusArrivalTiming("969", "58301")).getJSONArray("Services").length());
            assertFalse(0 == (lta.SingleBusArrivalTiming("105", "28029")).getJSONArray("Services").length());
            assertFalse(0 == (lta.SingleBusArrivalTiming("52", "28029")).getJSONArray("Services").length());
            assertFalse(0 == (lta.SingleBusArrivalTiming("188", "28029")).getJSONArray("Services").length());

            //invalid
            assertTrue(0 == (lta.SingleBusArrivalTiming("52", "92181")).getJSONArray("Services").length());
            assertTrue(0 == (lta.SingleBusArrivalTiming("16", "58301")).getJSONArray("Services").length());
            assertTrue(0 == (lta.SingleBusArrivalTiming("179", "58301")).getJSONArray("Services").length());
            assertTrue(0 == (lta.SingleBusArrivalTiming("969", "28029")).getJSONArray("Services").length());
            assertTrue(0 == (lta.SingleBusArrivalTiming("33", "28029")).getJSONArray("Services").length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getBusStops() {
        //Log.d("test",""+test);
        try {
            Context context = InstrumentationRegistry.getTargetContext();
            lta = new LTADatamallController(context);

            //valid
            assertTrue(0 < (lta.getBusStops(-1)).getJSONArray("value").length());
            //assertTrue(0 < (lta.getBusStops(0)).getJSONArray("value").length());
            //assertTrue(0 < (lta.getBusStops(471)).getJSONArray("value").length());
            assertTrue(0 < (lta.getBusStops(5002)).getJSONArray("value").length());

            //invalid
            //assertTrue(0 == (lta.getBusStops(-100)).getJSONArray("value").length());
            assertTrue(0 == (lta.getBusStops(-2)).getJSONArray("value").length());
            assertTrue(0 == (lta.getBusStops(5003)).getJSONArray("value").length());
            //assertTrue(0 == (lta.getBusStops(5100)).getJSONArray("value").length());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Test
    public void getBusRoute() {
        try{
            //valid
            assertTrue(0 < (lta.getBusRoute(-1)).getJSONArray("value").length());
            //assertTrue(0 < (lta.getBusRoute(0)).getJSONArray("value").length());
            //assertTrue(0 < (lta.getBusRoute(3442)).getJSONArray("value").length());
            assertTrue(0 < (lta.getBusRoute(26000)).getJSONArray("value").length());

            //invalid
            //assertTrue(0 == (lta.getBusRoute(-4322)).getJSONArray("value").length());
            assertTrue(0 == (lta.getBusRoute(-2)).getJSONArray("value").length());
            assertTrue(0 == (lta.getBusRoute(26001)).getJSONArray("value").length());
            //assertTrue(0 == (lta.getBusRoute(47520)).getJSONArray("value").length());
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void getBusArrivalByBusStopCode() {
        try{
            //valid
            assertFalse(0==(lta.getBusArrivalByBusStopCode("07351")).getJSONArray("Services").length());
            assertFalse(0==(lta.getBusArrivalByBusStopCode("19089")).getJSONArray("Services").length());
            assertFalse(0==(lta.getBusArrivalByBusStopCode("46399")).getJSONArray("Services").length());
            assertFalse(0==(lta.getBusArrivalByBusStopCode("84271")).getJSONArray("Services").length());
            assertFalse(0==(lta.getBusArrivalByBusStopCode("85029")).getJSONArray("Services").length());

            //invalid
            assertTrue(0==(lta.getBusArrivalByBusStopCode("1332")).getJSONArray("Services").length());
            assertTrue(0==(lta.getBusArrivalByBusStopCode("847729")).getJSONArray("Services").length());
            assertTrue(0==(lta.getBusArrivalByBusStopCode("021398")).getJSONArray("Services").length());
            assertTrue(0==(lta.getBusArrivalByBusStopCode("213")).getJSONArray("Services").length());
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}