import {Form} from "react-bootstrap";
import {Fragment, useEffect, useState} from "react";
import * as auth from "../../services/authenticationService"
import {useNavigate} from "react-router-dom";
import {MdCloudCircle} from "react-icons/md";

/**
 * @description Login page
 */
const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const [content, setContent] = useState(<Btn/>);
    const navigate = useNavigate();

    /**
     * @description turn on charging status
     */
    function loadOn() {
        setContent(<div className={"text-center mt-4"}>
            <span className="loader"/>
        </div>);
    }

    function Btn() {
        return (
            <div className={"d-flex justify-content-between mt-4"}>
                <a className={"mt-1 cursor-pointer text-color-grey-hover-aux font-size-16 text-decoration-none"}
                   onClick={() => navigate("/recover-password")}>
                    <span>Forgot the password?</span>
                </a>
                <input type="submit" value="Get into"
                       className={"w-30 btn-3-user bg-color-aux border-radius-main-2 box-shadow-main-2"}/>
            </div>)
    }

    /**
     * @description turn off charging status
     * @param message
     */
    function loadOff(message) {
        setMessage(message);
        setContent(<div><p className={"mt-3 p-1 font-size-18 text-color-red"}>{message}</p></div>);
    }

    /**
     *  @description set value input
     * @param value
     * @param setter
     */
    function read(value, setter) {
        if (message.length > 0) {
            setContent(<Btn/>);
            setMessage("");
        }
        setter(value);
    }

    /**
     * @description login
     */
        // @ts-ignore
    const handleSubmit = async (event: any) => {
            event.preventDefault();

            if (username.length === 0 || password.length === 0) {
                loadOff("Please fill all fields");
                return;
            }

            loadOn();

            await auth.userLogin({"username": username, "password": password})
                .then(response => {
                    if (response.status === 200) {
                        setTimeout(() => {
                            navigate("/dashboard", {
                                state: {
                                    token: response.data.token,
                                    userId: response.data.userId
                                }
                            });
                        }, 1000);
                    }
                })
                .catch(error => {
                    let mess: string;
                    if (error.response.status === 0)
                        mess = "We are having problems, try again later";
                    else if (error.response.status === 400 || error.response.status === 500)
                        mess = error.response.data

                    loadOff(mess);
                    setTimeout(() => {
                        setContent(<Btn/>);
                    }, 5000);
                });
        };

    /**
     * @description: Set initial state
     */
    useEffect(() => {
        if (localStorage.getItem("USER_KEY") !== null
            && localStorage.getItem("USER_ID") !== null) {
            navigate("/dashboard", {
                state: {
                    token: localStorage.getItem("USER_KEY"),
                    userId: localStorage.getItem("USER_ID")
                }
            });
        }
        document.title = "PayrollFile - Login";
    }, []);

    return (
        <Fragment>
            <div className={"m-0 vh-100 row justify-content-center align-content-center"}>
                <Form className={"form-main border-radius-main form-login box-shadow-main effect-main"} onSubmit={handleSubmit}>
                    <div className={"d-flex mb-2"}>
                        <MdCloudCircle className={"text-color-aux font-size-50"}/>
                        <h4 className={"ms-1 mt-2 text-color-grey"}>Payroll</h4>
                    </div>
                    <h3>Login to the system</h3>
                    <span className={"text-color-grey"}>Please fill in your credentials to login.</span>
                    <Form.Group controlId="formBasicUserName" className={"mt-3"}>
                        <Form.Label className={"text-color-grey"}>Username</Form.Label>
                        <input className={"input"} type="text"
                               onChange={(event: any) => read(event.target.value, setUsername)}
                               placeholder="Enter user name" required/>
                    </Form.Group>
                    <Form.Group controlId="formBasicPassword" className={"mt-3"}>
                        <Form.Label className={"text-color-grey"}>Password</Form.Label>
                        <input className={"input"} type="password"
                               onChange={(event: any) => read(event.target.value, setPassword)}
                               placeholder="Enter password" required/>
                    </Form.Group>
                    {content}
                </Form>
            </div>
        </Fragment>
    )
}

export default Login