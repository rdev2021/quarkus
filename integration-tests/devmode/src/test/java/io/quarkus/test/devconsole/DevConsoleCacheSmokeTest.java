package io.quarkus.test.devconsole;

import javax.inject.Singleton;

import org.hamcrest.Matchers;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.cache.CacheResult;
import io.quarkus.test.QuarkusDevModeTest;
import io.restassured.RestAssured;

/**
 * Note that this test cannot be placed under the relevant {@code -deployment} module because then the DEV UI processor would
 * not be able to locate the template resources correctly.
 */
public class DevConsoleCacheSmokeTest {

    @RegisterExtension
    static final QuarkusDevModeTest config = new QuarkusDevModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClass(MyBean.class));

    @Test
    public void testCaches() {
        RestAssured.get("q/dev/io.quarkus.quarkus-cache/caches")
                .then()
                .statusCode(200).body(Matchers.containsString("myCache"));
    }

    @Singleton
    public static class MyBean {

        @CacheResult(cacheName = "myCache")
        String ping() {
            return "foo";
        }

    }

}
