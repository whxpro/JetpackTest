package com.whx.jetpacktest.net

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class SSLSocketClient {
    companion object {
        fun getSSLSocketFactory(): SSLSocketFactory {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, getTrustManager(), SecureRandom())
            return sslContext.socketFactory
        }

        private fun getTrustManager(): Array<TrustManager> {
            return arrayOf(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                }

                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })
        }

        fun getHostnameVerifier(): HostnameVerifier {
            return HostnameVerifier { _, _ -> true }
        }
    }
}