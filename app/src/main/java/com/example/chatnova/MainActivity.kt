package com.example.chatnova

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var chatContainer: LinearLayout
    private lateinit var scrollView: ScrollView

    // Stores the conversation context
    private var lastTopic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        chatContainer = findViewById(R.id.chatContainer)
        scrollView = findViewById(R.id.scrollView)

        btnSend.setOnClickListener {
            sendMessage()
        }

        etMessage.setOnEditorActionListener { _, _, _ ->
            sendMessage()
            true
        }
    }

    private fun sendMessage() {

        val message = etMessage.text.toString().trim()

        if (message.isEmpty()) {
            return
        }

        // Display user message
        addUserMessage(message)

        // Clear input
        etMessage.text.clear()

        // Hide keyboard
        hideKeyboard()

        // Generate ChatNova response
        val response = generateResponse(message)

        // Display bot response
        addBotMessage(response)

        // Scroll to bottom
        scrollToBottom()
    }

    private fun generateResponse(message: String): String {

        val text = message.lowercase().trim()

        return when {

            // Greeting
            text.contains("hello") ||
                    text.contains("hi") ||
                    text.contains("hey") -> {

                lastTopic = "greeting"

                "Hello! 👋\n\nI'm ChatNova. How can I help you today?"
            }

            // User asks about ChatNova
            text.contains("who are you") ||
                    text.contains("what are you") -> {

                lastTopic = "chatnova"

                "I'm ChatNova 🤖, a conversational assistant created for this mobile application.\n\nI can answer questions and maintain simple conversation context."
            }

            // Android topic
            text.contains("android") -> {

                lastTopic = "android"

                "Android is a mobile operating system developed by Google. 📱\n\nIt is widely used for smartphones, tablets, TVs, watches, and other devices.\n\nWould you like to know about Android development?"
            }

            // Follow-up to Android
            (lastTopic == "android" &&
                    (text.contains("development") ||
                            text.contains("develop") ||
                            text.contains("how"))) -> {

                "Android development involves creating applications using tools such as Android Studio, Kotlin, Java, XML layouts, and Android SDK components.\n\nYou can build apps such as chat applications, games, healthcare apps, and many others."
            }

            // Kotlin
            text.contains("kotlin") -> {

                lastTopic = "kotlin"

                "Kotlin is a modern programming language developed by JetBrains. 💻\n\nIt is officially supported for Android development and is concise, safe, and interoperable with Java."
            }

            // Follow-up Kotlin
            lastTopic == "kotlin" &&
                    (text.contains("why") ||
                            text.contains("use")) -> {

                "Kotlin is popular for Android because it reduces boilerplate code, provides null-safety, supports modern programming features, and works well with existing Java code."
            }

            // AI
            text.contains("artificial intelligence") ||
                    text == "ai" ||
                    text.contains("what is ai") -> {

                lastTopic = "ai"

                "Artificial Intelligence (AI) is the field of computer science that develops systems capable of performing tasks that normally require human intelligence. 🤖\n\nExamples include chatbots, recommendation systems, computer vision, and speech recognition."
            }

            // Follow-up AI
            lastTopic == "ai" &&
                    (text.contains("example") ||
                            text.contains("examples")) -> {

                "Examples of AI include ChatGPT-style assistants, face recognition, voice assistants, medical diagnosis systems, fraud detection, and autonomous systems."
            }

            // Machine Learning
            text.contains("machine learning") ||
                    text.contains("what is ml") -> {

                lastTopic = "machine learning"

                "Machine Learning (ML) is a branch of AI where computers learn patterns from data and use those patterns to make predictions or decisions."
            }

            // Help
            text.contains("help") -> {

                "Sure! 😊\n\nYou can ask me things like:\n\n• What is Android?\n• Who are you?\n• What is AI?\n• What is Machine Learning?\n• What is Kotlin?\n• Tell me an example\n\nI can also handle simple follow-up questions."
            }

            // Thanks
            text.contains("thank") ||
                    text.contains("thanks") -> {

                "You're welcome! 😊\n\nI'm happy to help."
            }

            // Goodbye
            text.contains("bye") ||
                    text.contains("goodbye") -> {

                "Goodbye! 👋\n\nHave a great day. You can come back anytime to chat with ChatNova."
            }

            // Follow-up example
            text.contains("example") -> {

                when (lastTopic) {

                    "ai" -> {
                        "For example, a chatbot like ChatNova uses conversational logic to receive a user's question and provide an appropriate response."
                    }

                    "android" -> {
                        "For example, Android apps can be developed using Kotlin and XML in Android Studio."
                    }

                    "machine learning" -> {
                        "For example, an email spam detector can learn from previous emails and classify new emails as spam or not spam."
                    }

                    else -> {
                        "Sure! Please tell me the topic for which you want an example."
                    }
                }
            }

            // Default response
            else -> {

                "I understand your question. 🤔\n\nI'm currently a demonstration chatbot, so my knowledge is limited to the topics programmed into this application.\n\nTry asking:\n\n• What is Android?\n• What is AI?\n• What is Kotlin?\n• What is Machine Learning?"
            }
        }
    }

    private fun addUserMessage(message: String) {

        val row = LinearLayout(this)

        row.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = 12
        }

        row.gravity = Gravity.END

        val textView = TextView(this)

        textView.text = "👤  $message"
        textView.textSize = 16f
        textView.setTextColor(Color.WHITE)
        textView.setPadding(18, 14, 18, 14)

        textView.background =
            getDrawable(R.drawable.user_message_background)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.width = LinearLayout.LayoutParams.WRAP_CONTENT

        textView.layoutParams = params

        row.addView(textView)

        chatContainer.addView(row)
    }

    private fun addBotMessage(message: String) {

        val row = LinearLayout(this)

        row.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = 12
        }

        row.gravity = Gravity.START

        val textView = TextView(this)

        textView.text = "🤖  $message"
        textView.textSize = 16f
        textView.setTextColor(
            Color.parseColor("#202124")
        )

        textView.setPadding(18, 14, 18, 14)

        textView.background =
            getDrawable(R.drawable.bot_message_background)

        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        row.addView(textView)

        chatContainer.addView(row)
    }

    private fun scrollToBottom() {

        scrollView.post {
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun hideKeyboard() {

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(
            etMessage.windowToken,
            0
        )
    }
}