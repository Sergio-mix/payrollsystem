import Select from "react-select";
import {Fragment, useEffect, useState} from "react";
import {Form} from "react-bootstrap";
import {AiOutlineCaretLeft} from "react-icons/ai";
import {getAllCountryCode} from "../../services/countryCodeService";
import {getAllTypeDocument} from "../../services/typeDocumentService";
import {getAllAuthority} from "../../services/authorityService";
import {useNavigate} from "react-router-dom";
import {saveUser, updateUser} from "../../services/userService";

/**
 * @description Component that renders the user registration form
 * @param props event close
 * @constructor
 */
const UserForm = (props) => {

    const [title, setTitle] = useState(props.userData.title);
    const REGISTER = "register";
    const UPDATE = "update";
    const navigate = useNavigate();

    const btn = <input type={"submit"} value={props.userData.titleBtn}
                       className={"btn-sumit box-shadow-main-2 border-radius-main mb-2"}/>;//btn-sumit

    const [content, setContent] = useState(btn);
    const [load, setLoad] = useState(true);

    //list query
    const [countryCode, setCountryCode] = useState([]);
    const [typeDocument, setTypeDocument] = useState([]);
    const [authority, setAuthority] = useState([]);

    const [user, setUser] = useState({});//data user
    const [passwordConfirm, setPasswordConfirm] = useState("");
    const [authorities, setAuthorities] = useState([]);

    const countryCodeDefaultValue = (props.userData.action === UPDATE) ? {
        value: props.userData.user.countryCode,
        label: "(+" + props.userData.user.countryCode.code + ") " + props.userData.user.countryCode.country
    } : null;

    const typeDocumentDefaultValue = (props.userData.action === UPDATE) ? {
        value: props.userData.user.typeDocument,
        label: "(" + props.userData.user.typeDocument.name + ") " + props.userData.user.typeDocument.description
    } : null;

    const authorityDefaultValue = (props.userData.action === UPDATE) ? props.userData.user.authorities.map(authority => {
        return {
            value: {id: authority.id, roleCode: authority.roleCode, description: authority.description},
            label: authority.roleCode
        }
    }) : null;

    /**
     * @description: onload component
     */
    function loadOn() {
        setContent(<div><span className="loader"/></div>);
    }

    const query = async functionName =>
        await functionName.then(response => {
            return response.data;
        }).catch(error => {
            switch (error.response.status) {
                case 400:
                    return error;
                case 401:
                    navigate("/401");
                    break;
                case 404:
                    navigate("*");
                    break;
                case 500:
                    props.modal.open(
                        <div>
                            <p className={"font-size-25 text-color-red"}>{error.response.data}</p>
                        </div>
                    );
                    setContent(btn);
                    break;
                case 0:
                    navigate("/login");
                    break;
            }
        });

    const allCountryCodeQuery = async () => await query(getAllCountryCode());//get all country code
    const allTypeDocumentQuery = async () => await query(getAllTypeDocument());//get all type document
    const allAuthorityQuery = async () => await query(getAllAuthority());//get all authority
    const saveUserQuery = async (user) => await query(saveUser(user));//save user
    const updateUserQuery = async (user) => await query(updateUser(user));//update user

    function eventUser(user) {
        switch (props.userData.action) {
            case REGISTER:
                return saveUserQuery(user);
            case UPDATE:
                return updateUserQuery(user);
        }
    }

    /**
     * @description: all country code
     */
        // @ts-ignore
    const listCountryCode = async () => {
            let listSelect = [];
            for (let countryCode of await allCountryCodeQuery())
                listSelect.push({value: countryCode, label: "(+" + countryCode.code + ") " + countryCode.country})
            setCountryCode(listSelect);
        };

    /**
     * @description: all type document
     */
        // @ts-ignore
    const listTypeDocument = async () => {
            let listSelect = [];
            for (let typeDocument of await allTypeDocumentQuery())
                listSelect.push({value: typeDocument, label: "(" + typeDocument.name + ") " + typeDocument.description})
            setTypeDocument(listSelect);
        };

    /**
     * @description: all authority
     */
        // @ts-ignore
    const listAuthority = async () => {
            let listSelect = [];
            for (let authority of await allAuthorityQuery())
                listSelect.push({
                    value: {id: authority.id, roleCode: authority.roleCode, description: authority.description},
                    label: authority.roleCode
                })
            setAuthority(listSelect);
        };

    /**
     * @description: enter value to user
     * @param value {string} value to enter
     * @param attribute {string} attribute to enter
     */
    const setValue = (value, attribute) => {
        let ob = user;
        ob[attribute] = value
        setUser(ob);
    };

    /**
     * @description: remove error message
     * @param value {string} attribute to enter
     */
    function resetInput(value) {
        let ob = value + "-input";

        if (document.getElementById(ob).classList.contains("input-error")) {
            document.getElementById(value).innerText = "";
            document.getElementById(ob).classList.remove("input-error");
        }
    }

    function validateDataForm() {
        let status = true;

        if (user["countryCode"].length === 0 || user["countryCode"] === undefined) {
            document.getElementById("countryCode-input").classList.add("input-error");
            document.getElementById("countryCode").innerText = "Selecting an empty option is not allowed";
            status = false;
        }

        if (user["typeDocument"].length === 0 || user["typeDocument"] === undefined) {
            document.getElementById("typeDocument-input").classList.add("input-error");
            document.getElementById("typeDocument").innerText = "Selecting an empty option is not allowed";
            status = false;
        }

        if (authorities.length === 0) {
            document.getElementById("authorities-input").classList.add("input-error");
            document.getElementById("authorities").innerText = "Selecting an empty option is not allowed";
            status = false;
        }

        if (passwordConfirm !== user["password"]) {
            document.getElementById("password-confirm-input").classList.add("input-error");
            document.getElementById("password-confirm").innerText = "Password does not match";
            status = false;
        }


        if (props.userData.action === REGISTER)
            if (user["password"].length === 0) {
                document.getElementById("password-input").classList.add("input-error");
                document.getElementById("password").innerText = "password is required";
                status = false;
            }

        if (!status) {
            props.modal.open(
                <div>
                    <h1 className={"text-color-red"}>😞</h1>
                    <p className={"font-size-25"}>Please check the data you entered</p>
                </div>
            );
        }


        return status;
    }

    function addAuthority() {
        try {
            let userAux = user;
            let authoritiesList = [];

            authorities.forEach(authority => {
                authoritiesList.push(authority.value);
            });

            const search = authoritiesList.reduce((acc, authority) => {
                acc[authority.id] = ++acc[authority.id] || 0;
                return acc;
            }, {});

            const duplicates = authoritiesList.filter((authority) => {
                return search[authority.id];
            });

            if (duplicates.length !== 0) {
                document.getElementById("authorities-input").classList.add("input-error");
                document.getElementById("authorities").innerText = "Duplicate elements";
                props.modal.open(
                    <div>
                        <h1 className={"text-color-red"}>😞</h1>
                        <p className={"font-size-25"}>Please check the data you entered</p>
                    </div>
                );
                return false;
            }

            userAux["authorities"] = authoritiesList;
            setUser(userAux);
            return true;
        } catch (e) {
            return false;
        }
    }

    function ok(value) {
        props.modal.openIsCloseNot(
            <div>
                <p className={"text-color-aux font-size-27"}>{value}</p>
            </div>
        );

        setTimeout(() => {
            props.modal.close();
            props.closeOk();//close event
        }, 1500);
    }

    function error400(list) {
        list.forEach(error => {
            document.getElementById(error.attribute + "-input").classList.add("input-error");//add class input-error

            if (error.message !== null) {
                document.getElementById(error.attribute).innerText = error.message;//show message
            }
        });

        props.modal.open(
            <div>
                <h1>😞</h1>
                <p className={"font-size-25"}>Please check the data you entered</p>
            </div>
        );

        setContent(btn);
    }

    /**
     * @description: save user
     * @param event {object} event
     */
        // @ts-ignore
    const userOnClick = async (event) => {
            event.preventDefault();
            loadOn();

            if (!validateDataForm()) {
                setContent(btn);
                return;
            }

            if (!addAuthority()) {
                setContent(btn);
                return;
            }

            await eventUser(user).then(response => {
                if (typeof response === "string") {
                    ok(response);
                }
                //response is an object
                if (response.response.status === 400) {
                    error400(response.response.data);
                }
            });
        };

    function constUpdate() {
        if (props.userData.action === UPDATE) {
            let userUpdate = props.userData.user;
            try {
                for (let key in userUpdate) {
                    if (userUpdate[key] !== null
                        && document.body.contains(document.getElementById(key + "-input"))
                        && key !== "password") {
                        setValue(userUpdate[key], key);
                        // @ts-ignore
                        document.getElementById(key + "-input").value = userUpdate[key];
                    }
                }

                setValue(countryCodeDefaultValue.value, "countryCode");
                setValue(typeDocumentDefaultValue.value, "typeDocument");
                setAuthorities(authorityDefaultValue);
            } catch
                (e) {
            }
        }
    }

    /**
     * @description: Set initial state
     */
    useEffect(() => {
        let userAux = user;

        userAux["id"] = (props.userData.action === UPDATE) ? props.userData.user.id : "";
        userAux["countryCode"] = (props.userData.action === UPDATE) ? countryCodeDefaultValue : [];
        userAux["typeDocument"] = (props.userData.action === UPDATE) ? typeDocumentDefaultValue : [];
        userAux["authorities"] = [];
        user["password"] = "";
        setUser(userAux);

        listCountryCode().then(r => null);
        listTypeDocument().then(r => null);
        listAuthority().then(r => setLoad(false));
        constUpdate();
    }, []);


    return (
        <Fragment>
            <Form className={"div-responsive mb-5 effect-main"} onSubmit={userOnClick}>
                <div className={"form-main border-radius-main form-first box-shadow-main"}>

                    <h1>{title}</h1>

                    <div className="row">
                        <div className="col-sm-6">
                            <Form.Label>Names <span className={"text-color-red"}>*</span></Form.Label>
                            <input id={"names-input"} className={"input"} type={"text"}
                                   onChange={(event: any) => {
                                       setValue(event.target.value, "names"),
                                           resetInput("names")
                                   }}
                                   placeholder={"Names"} required/>
                            <Form.Text id={"names"} className={"text-color-red"}></Form.Text>
                        </div>

                        <div className="col-sm-6">
                            <Form.Label>Last names <span className={"text-color-red"}>*</span></Form.Label>
                            <input id={"lastNames-input"} className={"input"} type={"text"}
                                   onChange={(event: any) => {
                                       setValue(event.target.value, "lastNames"), resetInput("lastNames")
                                   }}
                                   placeholder={"Last names"} required/>
                            <Form.Text id={"lastNames"} className={"text-color-red"}></Form.Text>
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-sm-6 mt-2">
                            <Form.Label>Country <span className={"text-color-red"}>*</span></Form.Label>
                            <Select id={"countryCode-input"}
                                    options={countryCode}
                                    defaultValue={countryCodeDefaultValue}
                                    onChange={(event: any) => {
                                        setValue(event.value, "countryCode"), resetInput("countryCode")
                                    }}
                                    isLoading={load}/>
                            <Form.Text id={"countryCode"} className={"text-color-red"}></Form.Text>
                        </div>

                        <div className="col-sm-6">
                            <Form.Label>Mobile <span className={"text-color-red"}>*</span></Form.Label>
                            <input id={"cellPhone-input"} className={"input"} type={"number"}
                                   onChange={(event: any) => {
                                       setValue(event.target.value, "cellPhone"), resetInput("cellPhone")
                                   }}
                                   placeholder={"Mobile"} maxLength={15} required/>
                            <Form.Text id={"cellPhone"} className={"text-color-red"}></Form.Text>
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-sm-6 mt-2">
                            <Form.Label>Type <span className={"text-color-red"}>*</span></Form.Label>
                            <Select id={"typeDocument-input"} options={typeDocument}
                                    defaultValue={typeDocumentDefaultValue}
                                    onChange={(event: any) => {
                                        setValue(event.value, "typeDocument"), resetInput("typeDocument")
                                    }}
                                    isLoading={load}/>
                            <Form.Text id={"typeDocument"} className={"text-color-red"}></Form.Text>
                        </div>

                        <div className="col-sm-6">
                            <Form.Label>Document <span className={"text-color-red"}>*</span></Form.Label>
                            <input id={"documentNumber-input"} className={"input"} type={"number"}
                                   onChange={(event: any) => {
                                       setValue(event.target.value, "documentNumber"), resetInput("documentNumber")
                                   }}
                                   placeholder={"Document"} minLength={1} maxLength={15} required/>
                            <Form.Text id={"document"} className={"text-color-red"}></Form.Text>
                        </div>
                    </div>

                    <div className="row text-center mt-4">
                        <div className="col-sm-12">
                            {content}
                        </div>
                    </div>

                    <span onClick={props.close}
                          className={"font-size-18 text-color-grey-hover-aux cursor-pointer"}>
                <AiOutlineCaretLeft/> back</span>
                </div>

                <div className={"form-main border-radius-main form-aux box-shadow-main"}>

                    <h2>Auth</h2>

                    <div className="row">
                        <div className="col-sm-12">
                            <Form.Label>Mail <span className={"text-color-red"}>*</span></Form.Label>
                            <input id={"email-input"} className={"input"} type={"email"}
                                   onChange={(event: any) => {
                                       setValue(event.target.value, "email"), resetInput("email")
                                   }}
                                   placeholder={"Mail"} required/>
                            <Form.Text id={"email"} className={"text-color-red"}></Form.Text>
                        </div>

                        <div className="col-sm-6">
                            <Form.Label>Password {(props.userData.action === REGISTER ?
                                <span className={"text-color-red"}>*</span> : null)}</Form.Label>
                            <input id={"password-input"} className={"input"} type={"password"}
                                   onChange={(event: any) => {
                                       setValue(event.target.value, "password"), resetInput("password")
                                   }}
                                   placeholder={"Password"}/>
                            <Form.Text id={"password"} className={"text-color-red"}></Form.Text>
                        </div>

                        <div className="col-sm-6">
                            <Form.Label>Confirm {(props.userData.action === REGISTER ?
                                <span className={"text-color-red"}>*</span> : null)}</Form.Label>
                            <input id={"password-confirm-input"} className={"input"} type={"password"}
                                   onChange={(event: any) => {
                                       setPasswordConfirm(event.target.value), resetInput("password-confirm")
                                   }}
                                   placeholder={"Password"}/>
                            <Form.Text id={"password-confirm"} className={"text-color-red"}></Form.Text>
                        </div>

                        <div className="col-sm-6">
                            <Form.Label>Expiration <span
                                className={"text-color-red"}>*</span></Form.Label>
                            <input id={"daysToExpire-input"} className={"input"} type={"number"} min={1}
                                   onChange={(event: any) => {
                                       setValue(parseInt(event.target.value),
                                           "daysToExpire"), resetInput("daysToExpire")
                                   }}
                                   placeholder={"Days"} required/>
                            <Form.Text id={"daysToExpire"} className={"text-color-red"}></Form.Text>
                        </div>

                        <div className="col-sm-6">
                            <Form.Label>Username <span className={"text-color-red"}>*</span></Form.Label>
                            <input id={"username-input"} className={"input"} type={"text"}
                                   onChange={(event: any) => {
                                       setValue(event.target.value, "username"), resetInput("username")
                                   }}
                                   placeholder={"Username"} required/>
                            <Form.Text id={"username"} className={"text-color-red"}></Form.Text>
                        </div>

                        <div className="col-sm-12">
                            <Form.Label>Permissions <span className={"text-color-red"}>*</span></Form.Label>
                            <Select
                                id={"authorities-input"}
                                isMulti
                                defaultValue={authorityDefaultValue}
                                isLoading={load}
                                options={authority}
                                closeMenuOnSelect={false}
                                onChange={(event: any) => {
                                    setAuthorities(event), resetInput("authorities")
                                }}
                            />
                            <Form.Text id={"authorities"} className={"text-color-red"}></Form.Text>
                        </div>
                    </div>
                </div>
            </Form>
        </Fragment>
    )
}

export default UserForm;