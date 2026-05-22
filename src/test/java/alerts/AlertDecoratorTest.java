package alerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alerts.Alert;
import com.alerts.BasicAlert;
import com.alerts.PriorityAlertDecorator;
import com.alerts.RepeatedAlertDecorator;
import org.junit.jupiter.api.Test;

class AlertDecoratorTest {

    @Test
    void priorityDecoratorChangesPriorityOnly() {
        Alert baseAlert = new BasicAlert("1", "Critical high systolic pressure", 1000L);
        Alert decoratedAlert = new PriorityAlertDecorator(baseAlert, "HIGH");

        assertEquals("1", decoratedAlert.getPatientId());
        assertEquals("Critical high systolic pressure", decoratedAlert.getCondition());
        assertEquals(1000L, decoratedAlert.getTimestamp());
        assertEquals("HIGH", decoratedAlert.getPriority());
    }

    @Test
    void repeatedDecoratorMarksAlertAsRepeatable() {
        Alert baseAlert = new BasicAlert("1", "Manual alert triggered", 1000L);
        Alert decoratedAlert = new RepeatedAlertDecorator(baseAlert);

        assertEquals("1", decoratedAlert.getPatientId());
        assertEquals("Manual alert triggered", decoratedAlert.getCondition());
        assertTrue(decoratedAlert.shouldRepeat());
    }
}
