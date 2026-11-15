package es.kyoe.ing.mortgage.web.infrastructure.rest.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Endpoint(id = "jolokia")
public class JolokiaActuatorEndpoint {

    @ReadOperation
    public Map<String, Object> jolokiaInfo() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memoryMxBean = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("product", "spring-boot");
        info.put("vendor", "Spring");
        info.put("version", "4.0.6");

        Map<String, Object> runtime = new LinkedHashMap<>();
        runtime.put("vmName", runtimeMxBean.getVmName());
        runtime.put("vmVersion", runtimeMxBean.getVmVersion());
        runtime.put("uptimeMs", runtimeMxBean.getUptime());
        runtime.put("heapUsedBytes", memoryMxBean.getHeapMemoryUsage().getUsed());
        runtime.put("heapMaxBytes", memoryMxBean.getHeapMemoryUsage().getMax());
        runtime.put("threadCount", threadMxBean.getThreadCount());

        Map<String, Object> value = new LinkedHashMap<>();
        value.put("agent", "2.1.0");
        value.put("protocol", "7.2");
        value.put("mbeanCount", mBeanServer.getMBeanCount());
        value.put("domains", Arrays.asList(mBeanServer.getDomains()));
        value.put("info", info);
        value.put("runtime", runtime);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("request", Map.of("type", "version"));
        response.put("value", value);
        response.put("timestamp", Instant.now().getEpochSecond());
        response.put("status", 200);
        return response;
    }
}

