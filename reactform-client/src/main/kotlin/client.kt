import common.types.Name
import kotlinx.css.*
import kotlinx.css.properties.BoxShadows
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import kotlinx.serialization.json.JSON
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.fetch.RequestInit
import react.*
import react.dom.render
import styled.*
import kotlin.browser.document
import kotlin.browser.window

fun main(args: Array<String>) {
    render(document.getElementById("root")) {
        app()
    }
}


interface AppState : RState {
    var firstName: String
    var touched: HashSet<String>
}

data class Errors(
        var firstName: String?
)

class App : RComponent<RProps, AppState>() {
    override fun AppState.init() {
        firstName = ""
        touched = HashSet()
    }

    private fun validate(inputs: String): Errors {
        return if (inputs.length > 1) {
            Errors(firstName = null)
        } else {
            Errors("Name should be longer than one character")
        }
    }

    private fun handleBlur(event: Event) {
        val target = event.target as HTMLInputElement
        setState {
            touched.add(target.id)
        }
    }

    private fun handleFirstNameChange(event: Event) {
        val target = event.target as HTMLInputElement
        setState {
            firstName = target.value
        }
    }

    private fun handleSubmit(event: Event) {
        event.preventDefault()
        if (state.firstName.isNotBlank()) {
            val postData = Name(state.firstName)
            val requestOpts = RequestInit(
                    method = "POST",
                    body = JSON.stringify(postData),
                    referrerPolicy = "",
                    integrity = ""
            )

            window.fetch("http://localhost:7000", requestOpts)
                    .then {
                        console.log("Data sent successfully!")
                        console.log(postData)
                    }
                    .catch {
                        console.log(it)
                    }
        } else {
            window.fetch("http://localhost:7000")
                    .then {
                        console.log("Data received successfully!")
                        console.log(it)
                    }
        }
    }

    override fun RBuilder.render() {
        val errors = validate(inputs = state.firstName)
        styledDiv {
            css { +Styles.root }

            styledForm {
                css { +Styles.form }

                attrs {
                    onSubmitFunction = {
                        handleSubmit(it)
                    }
                }

                styledLabel {
                    css { +Styles.label }

                    attrs { htmlFor = "Name" }
                    +"First Name"
                }
                styledInput {
                    css { +Styles.input }

                    attrs {
                        id = "Name"
                        type = InputType.text
                        placeholder = "Enter first name"
                        value = state.firstName
                        onBlurFunction = {
                            handleBlur(it)
                        }
                        onChangeFunction = {
                            handleFirstNameChange(it)
                        }
                    }
                }

                if (!errors.firstName.isNullOrEmpty() && state.touched.contains("Name")) {
                    styledP {
                        css { +Styles.error }

                        +errors.firstName!!
                    }
                }
                styledButton {
                    css { +Styles.submit }

                    if (state.firstName.isNotBlank()) {
                        +"Sign up as ${state.firstName}"
                    } else {
                        +"Sign up"
                    }
                }
            }
        }
    }
}


object Styles : StyleSheet("Styles", false) {
    val root by css {
        paddingTop = 50.px
        display = Display.flex
        alignItems = Align.center
        justifyContent = JustifyContent.center
        fontFamily = "-apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto,Oxygen-Sans,Ubuntu, Cantarell, \"Helvetica Neue\", sans-serif"
        fontSize = 18.px
    }

    val form by css {
        display = Display.flex
        flexWrap = FlexWrap.wrap
        width = 400.px
    }

    val label by css {
        padding(16.px)
        alignSelf = Align.center
        justifyContent = JustifyContent.center
    }

    val input by css {
        padding(16.px)
        margin(top = 16.px, bottom = 16.px, left = 16.px, right = 0.px)
        fontSize = 16.px
        flex(1.0)
        borderColor = Color("#ABC798")
        outline = Outline.none
        boxShadow = BoxShadows.none
        color = Color("palevioletred")

        placeholder {
            color = Color("palevioletred")
        }
        focus {
            borderColor = Color("palevioletred")
            borderWidth = 2.px
            outline = Outline.none
            boxShadow = BoxShadows.none
        }
    }

    val error by css {
        color = Color("papayawhip")
        backgroundColor = Color("palevioletred")
        padding(14.px)
        width = 100.pct
        textAlign = TextAlign.center
    }

    val submit by css {
        padding(16.px)
        fontSize = 18.px
        backgroundColor = Color("papayawhip")
        color = Color("palevioletred")
        borderColor = Color.transparent
        width = 100.pct
        focus {
            borderColor = Color("palevioletred")
            borderWidth = 2.px
            outline = Outline.none
            boxShadow = BoxShadows.none
        }
    }
}

fun RBuilder.app() = child(App::class) {}
