import {Fragment, useEffect, useRef, useState} from "react"
import {useNavigate, useParams} from "react-router-dom";
import {AiOutlineMail} from "react-icons/ai";
import * as auth from "../../services/authenticationService";
import {MdCloudCircle} from "react-icons/md";


const RecoverPasswordCode = () => {
    let codeData = null;

    function setCodeData(data) {
        codeData = data;
    }

    const codeUser = useRef(null);
    const password = useRef(null);
    const passwordConfirm = useRef(null);
    const [contentForm, setContentForm] = useState(null);
    const [contentIn, setContentIn] = useState(null);

    const navigate = useNavigate();
    let {id, email} = useParams();

    /**
     * @description turn on charging status
     */
    function loadOn() {
        setContentIn(<div className={"text-center mt-4"}>
            <span className="loader"/>
        </div>);
    }

    /**
     * @description turn off charging status
     * @param message
     */
    function loadOff(message) {
        setContentIn(<div className={"text-center mt-4"}><p
            className={"mt-3 p-1 font-size-18 text-color-red"}>{message}</p></div>);
    }

    /**
     * @description turn ok charging status
     * @param message
     */
    function loadOk(message) {
        setContentIn(<div className={"text-center mt-4"}><p
            className={"mt-3 p-1 font-size-18 text-color-green"}>{message}</p></div>);
    }

    function BtnVerifyCode() {
        return (
            <Fragment>
                <div className={"mt-5 d-flex justify-content-between"}>
                    <a className={"cursor-pointer text-color-grey-hover-aux font-size-18 text-decoration-none"}
                       onClick={() => navigate("/login")}>
                        <span>Login</span>
                    </a>

                    <input className={"w-auto btn-3-user bg-aux-color border-radius-main-2 box-shadow-main-2"}
                           type={"submit"}
                           value={"Send code"} onClick={verifyCode}/>
                </div>
            </Fragment>
        )
    }

    async function verifyCode() {
        loadOn();
        if (validateTextInput(codeUser, <BtnVerifyCode/>, "Code is required")) {
            await auth.verifyCode(Number(id), codeUser.current.value, email).then(response => {
                if (response.status === 200) {
                    setCodeData(codeUser.current.value);
                    setContentForm(<FormRegisterPassword/>);
                    setContentIn(<BtnRegisterPassword/>);
                }
            }).catch(error => {
                loadOff(error.response.data);
                setTimeout(() => {
                    setContentIn(<BtnVerifyCode/>);
                }, 3000);
            });
        }
    }

    function FormVerifyCode() {
        return (
            <Fragment>
                <span className={"text-color-grey"}>This is the email associated with the account</span>
                <div className={"mt-2 mb-2"}>
                    <AiOutlineMail className={"text-color-aux font-size-20"}/>
                    <span className={"ms-2"}>{email}</span>
                </div>
                <span className={"text-color-grey"}>Enter the code that we send to your email, remember that the code has a time limit</span>
                <div className={"text-center mt-3"}>
                    <input className={"input-2 text-center font-size-20 w-90"} type={"text"} ref={codeUser}
                           placeholder={"#code"} required/>
                </div>
            </Fragment>
        )
    }

    function BtnRegisterPassword() {
        return (
            <Fragment>
                <div className={"mt-3 text-center"}>
                    <input className={"btn w-90 bg-detail-color-main border-radius-main-2 box-shadow-main-2"}
                           type={"submit"} onClick={registerNewPassword} value={"Update"}/>
                </div>
            </Fragment>
        )
    }

    async function registerNewPassword() {
        loadOn();
        if (validateTextInput(password, <BtnRegisterPassword/>, "Password is required")
            && validateTextInput(passwordConfirm, <BtnRegisterPassword/>, "Password confirm is required")) {
            let pass = password.current.value;
            if (pass === passwordConfirm.current.value) {
                await auth.registerNewPassword(Number(id), codeData, pass, email).then(response => {
                    if (response.status === 200) {
                        loadOk(response.data);
                        setTimeout(() => {
                            navigate("/login");
                        }, 2500);
                    }
                }).catch(error => {
                    loadOff(error.response.data);
                    setTimeout(() => {
                        setContentIn(<BtnRegisterPassword/>);
                    }, 3000);
                });
            } else {
                loadOff("Password confirm is not equal password");
                setTimeout(() => {
                    setContentIn(<BtnRegisterPassword/>);
                }, 2000);
            }
        }
    }

    function FormRegisterPassword() {
        return (
            <Fragment>
                <span className={"text-color-grey"}>Please enter the new password</span>
                <div className={"mt-3 text-center"}>
                    <input ref={password} className={"input-2 w-80"} placeholder={"Password"}
                           type={"password"}/>
                </div>

                <div className={"mt-3 text-center"}>
                    <input ref={passwordConfirm} className={"input-2 w-80"} placeholder={"Confirm Password"}
                           type={"password"}/>
                </div>
            </Fragment>
        )
    }

    function validateTextInput(input, btn, message) {
        let value = input.current.value;
        if (value === undefined || value === "") {
            loadOff(message);
            input.current.focus();
            setTimeout(() => {
                setContentIn(btn);
            }, 2000);
            return false;
        }
        return true;
    }

    useEffect(() => {
        document.title = "Payroll - Restore password";
        setContentForm(<FormVerifyCode/>);
        setContentIn(<BtnVerifyCode/>);
    }, []);

    return (
        <Fragment>
            <div className={"m-0 vh-100 row justify-content-center align-content-center"}>
                <div className={"form-main border-radius-main form-login box-shadow-main effect-main"}>
                    <div className={"d-flex mb-2"}>
                        <MdCloudCircle className={"text-color-aux font-size-50"}/>
                        <h4 className={"ms-1 mt-2 text-color-grey"}>Payroll</h4>
                    </div>
                    <h3>Recover password</h3>
                    {contentForm}
                    {contentIn}
                </div>
            </div>
        </Fragment>
    )
}

export default RecoverPasswordCode;