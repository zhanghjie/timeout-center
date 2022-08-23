import com.common.timeout.client.tools.timewheel.TimerWheelService;
import com.common.timeout.client.tools.timewheel.vo.TimerTask;
import com.common.timeout.start.ApplicationStartup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/8/18 18:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimerTest {


    @Autowired
    private TimerWheelService timerWheelService;

    @Test
    public void test() {
        TimerTask timerTask = new TimerTask("timerTask", "timerTask", System.currentTimeMillis() + 1000);
        timerWheelService.add(timerTask);
        System.out.println(1);
    }
}
