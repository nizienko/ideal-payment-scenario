package content.back

import com.google.gson.Gson
import content.back.ApiClient.HttpMethod.*
import content.back.data.*
import org.apache.commons.codec.binary.Base64
import org.apache.http.client.fluent.Request
import org.apache.http.client.fluent.Request.*
import org.apache.http.entity.ContentType
import java.net.URI
import java.nio.charset.Charset

import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import assertk.assert
import assertk.assertions.isEqualTo
import content.ApiKey
import engine.utils.addAttachment
import engine.utils.step
import engine.utils.toData

private const val HMAC = "HmacSHA256"

fun apiClient(key: ApiKey) = ApiClient(key)

class ApiClient(private val key: ApiKey) {
    private val gson = Gson()
    private val utf8 = Charset.forName("UTF-8")
    private val serverUrl = "https://eu.sandbox.api-ingenico.com"
    private val merchantId = 2950

    fun hostedCheckoutsRequest(order: Order): HostedCheckoutResponse = step("Create hosted checkout") {
        val requestBody = HostedCheckoutRequest(order)
        val uri = URI("$serverUrl/v1/$merchantId/hostedcheckouts")

        val xGcsParameters = listOf(
            "X-GCS-MessageId" to uuid(),
            "X-GCS-RequestId" to uuid()
        )
        val date = Date()
        val contentType = "application/json"
        val signature = signData(POST, contentType, date, xGcsParameters, uri)

        val response = Request.Post(uri)
            .setHeader("Content-Type", contentType)
            .setDate(date).apply {
                xGcsParameters.forEach {
                    addHeader(it.first, it.second)
                }
            }
            .setHeader("Authorization", "GCS v1HMAC:${key.id}:$signature")
            .bodyString(
                gson.toJson(requestBody).apply {
                    addAttachment("request", this)
                },
                ContentType.APPLICATION_JSON
            )
            .execute()
            .returnResponse()

        val responseCode = response.statusLine.statusCode
        val responseBody = String(response.entity.content.readBytes())

        addAttachment("response", responseBody)

        assert(responseCode).isEqualTo(201)
        return@step gson.toData(responseBody)
    }

    private fun signData(
        method: HttpMethod = GET,
        contentType: String? = null,
        date: Date,
        xGcsParameters: List<Pair<String, String>> = emptyList(),
        uri: URI
    ): String {
        val dataToSign = StringBuffer().apply {
            append(method.name).n()
            append(contentType ?: "").n()
            append(date.toText()).n()
            xGcsParameters.stream()
                .sorted { o1, o2 -> o1.first.compareTo(o2.first) }
                .forEach {
                    append("${it.first.toLowerCase()}:${it.second}").n()
                }
            append(uri.path()).n()
        }
        val alg = Mac.getInstance(HMAC).apply {
            init(SecretKeySpec(key.secretKey.toByteArray(utf8), HMAC))
        }
        return Base64.encodeBase64String(alg.doFinal(dataToSign.toString().toByteArray(utf8)))
    }

    private fun URI.path(): String {
        val rawPath = this.rawPath
        return if (this.query == null) {
            rawPath
        } else {
            rawPath + '?'.toString() + this.query
        }
    }

    private fun uuid() = UUID.randomUUID().toString()
    private fun StringBuffer.n() = this.append("\n")
    private fun Date.toText(): String {
        val dateFormatter = SimpleDateFormat(DATE_FORMAT, DATE_LOCALE)
        dateFormatter.timeZone = TIME_ZONE
        return dateFormatter.format(this)
    }

    enum class HttpMethod { GET, POST, PUT, DELETE }
}

