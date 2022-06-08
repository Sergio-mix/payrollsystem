import {Fragment, useEffect, useRef, useState} from "react";
import * as auth from "../../services/authenticationService";
import {useNavigate} from "react-router-dom";
import {Form} from "react-bootstrap";
import {MdCloudCircle} from "react-icons/md";

const RecoverPasswordUsername = () => {
    const username = useRef(null);
    const navigate = useNavigate();
    const [content, setContent] = useState(null);


    function loadOn() {
        setContent(<div className={"text-center mt-5"}>
            <span className="loader"/>
        </div>);
    }

    function loadOff(message) {
        setContent(<div className={"mt-4"}><p
            className={"mt-3 p-1 font-size-18 text-color-grey"}>{message}</p></div>);
    }

    function Btn() {
        return (
            <Fragment>
                <div className={"mt-5 d-flex justify-content-between"}>
                    <a className={"cursor-pointer text-color-grey-hover-aux font-size-18 text-decoration-none"}
                       onClick={() => navigate("/login")}>
                        <span>Login</span>
                    </a>

                    <input className={"w-30 btn-3-user bg-color-aux border-radius-main-2 box-shadow-main-2"}
                           type={"submit"}
                           value={"Next"}/>
                </div>
            </Fragment>
        )
    }

    async function sendMail(e) {
        e.preventDefault();
        loadOn();
        await auth.restorePassword(username.current.value).then(response => {
            if (response.status === 200) {
                setTimeout(() => {
                    loadOff(response.data);
                    setTimeout(() => {
                        navigate("/login");
                    }, 6000);
                }, 3000);
            }
        }).catch(error => {
            loadOff(error.response.data);
            setTimeout(() => {
                setContent(<Btn/>);
            }, 3000);
        });
    }


    useEffect(() => {
        document.title = "PayrollFile - Restore password";
        setContent(<Btn/>);
    }, []);

    return (
        <Fragment>
            <div className={"m-0 vh-100 row justify-content-center align-content-center"}>
                <Form className={"form-main border-radius-main form-login box-shadow-main effect-main"} onSubmit={sendMail}>
                    <div className={"d-flex mb-2"}>
                        <MdCloudCircle className={"text-color-aux font-size-50"}/>
                        <h4 className={"ms-1 mt-2 text-color-grey"}>Payroll</h4>
                    </div>
                    <h3>Recover password</h3>
                    <span className={"text-color-grey"}>Please enter your username</span>
                    <div className={"text-center"}>
                        <input className={"input-2 text-center mt-4 font-size-20 w-80"} type={"text"} ref={username}
                               placeholder={"Username"} required/>
                    </div>

                    {content}
                </Form>
            </div>
        </Fragment>
    )
}

export default RecoverPasswordUsername;