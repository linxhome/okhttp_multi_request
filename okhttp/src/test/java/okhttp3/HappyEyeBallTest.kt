/*
 * Copyright (C) 2019 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package okhttp3

import java.net.InetAddress
import okhttp3.TestUtil.assumeNetwork
import okhttp3.internal.platform.OpenJSSEPlatform
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.testing.PlatformRule
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.openjsse.sun.security.ssl.SSLSocketFactoryImpl
import org.openjsse.sun.security.ssl.SSLSocketImpl

class HappyEyeBallTest {
  @JvmField @Rule var platform = PlatformRule()
  @JvmField @Rule val clientTestRule = OkHttpClientTestRule()
  @JvmField @Rule val server = MockWebServer()
  val dns = object:Dns {
    override fun lookup(hostname: String): List<InetAddress> {
      if(hostname == "www.taobao.com") {
        return listOf<InetAddress>(InetAddress.getByName("113.96.109.101"),
                InetAddress.getByName("113.96.109.100"))
      }
      return Dns.SYSTEM.lookup(hostname)
    }
  }
  val client = OkHttpClient.Builder().dns(dns).build()


  @Test
  fun testHappyEyeBall() {
    println("start happy eyeball")
    assertThat("start happy")
    val resp = client.newCall(Request.Builder().url("https://www.taobao.com").build()).execute()
    assertThat(resp.code == 200)
  }
}
