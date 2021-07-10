package me.rerere.zhiwang.util.format

import org.jsoup.internal.StringUtil
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.NodeTraversor
import org.jsoup.select.NodeVisitor

object HtmlFormatUtil {
    fun getPlainText(element: Element?): String? {
        val formatter = FormattingVisitor()
        NodeTraversor.traverse(
            formatter,
            element
        ) // walk the DOM, and call .head() and .tail() for each node
        return formatter.toString()
    }

    // the formatting rules, implemented in a breadth-first DOM traverse
    private class FormattingVisitor : NodeVisitor {
        private var width = 0
        private val accum = StringBuilder() // holds the accumulated text

        // hit when the node is first seen
        override fun head(node: Node, depth: Int) {
            val name: String = node.nodeName()
            if (node is TextNode) append((node as TextNode).text()) // TextNodes carry all user-readable text in the DOM.
            else if (name == "li") append("\n * ") else if (name == "dt") append("  ") else if (StringUtil.`in`(
                    name,
                    "p",
                    "h1",
                    "h2",
                    "h3",
                    "h4",
                    "h5",
                    "tr"
                )
            ) append("\n")
        }

        // hit when all of the node's children (if any) have been visited
        override fun tail(node: Node, depth: Int) {
            val name: String = node.nodeName()
            if (StringUtil.`in`(
                    name,
                    "br",
                    "dd",
                    "dt",
                    "p",
                    "h1",
                    "h2",
                    "h3",
                    "h4",
                    "h5"
                )
            ) append("\n") else if (name == "a") append(
                java.lang.String.format(
                    " <%s>",
                    node.absUrl("href")
                )
            )
        }

        // appends text to the string builder with a simple word wrap method
        private fun append(text: String) {
            if (text.startsWith("\n")) width =
                0 // reset counter if starts with a newline. only from formats above, not in natural text
            if (text == " " &&
                (accum.length == 0 || StringUtil.`in`(accum.substring(accum.length - 1), " ", "\n"))
            ) return  // don't accumulate long runs of empty spaces
            if (text.length + width > maxWidth) { // won't fit, needs to wrap
                val words = text.split("\\s+").toTypedArray()
                for (i in words.indices) {
                    var word = words[i]
                    val last = i == words.size - 1
                    if (!last) // insert a space if not the last word
                        word = "$word "
                    if (word.length + width > maxWidth) { // wrap and reset counter
                        accum.append("\n").append(word)
                        width = word.length
                    } else {
                        accum.append(word)
                        width += word.length
                    }
                }
            } else { // fits as is, without need to wrap text
                accum.append(text)
                width += text.length
            }
        }

        override fun toString(): String {
            return accum.toString()
        }

        companion object {
            private const val maxWidth = 80
        }
    }
}