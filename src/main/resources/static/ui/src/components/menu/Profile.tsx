import {Form} from "react-bootstrap";
import {Fragment, useEffect, useState} from "react";

/**
 *  @description: Profile component
 * @param props user data
 */
const Profile = (props) => {
    const [userData, setUserData] = useState(props.userData);// user data
    const [date, setDate] = useState(<span className={"spinner-border text-color-aux"}/>);// date expiration

    const [formProfileContainer, setFormProfileContainer] = useState(null);// form profile container

    const [authorities, setAuthorities] = useState(null);

    function FormProfile(props) {
        return (
            <Fragment>
                <Form.Group className={"container"}>
                    <div className="row">
                        <div className="col-sm-6">
                            <Form.Label>Names</Form.Label>
                            <span className={"input-static"}>{props.user.names}</span>
                        </div>
                        <div className="col-sm-6">
                            <Form.Label>Last names</Form.Label>
                            <span className={"input-static"}>{props.user.lastNames}</span>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-sm-6">
                            <Form.Label>Mobile</Form.Label>
                            <span
                                className={"input-static"}>{"(+" + props.user.countryCode.code + ") " + props.user.cellPhone}</span>
                        </div>
                        <div className="col-sm-6">
                            <Form.Label>Username</Form.Label>
                            <span className={"input-static"}>{props.user.username}</span>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-sm-6">
                            <Form.Label>Mail</Form.Label>
                            <span className={"input-static"}>{props.user.email}</span>
                        </div>
                        <div className="col-sm-6">
                            <Form.Label>Document</Form.Label>
                            <span
                                className={"input-static"}>
                                            {"(" + props.user.typeDocument.name + ") " + props.user.documentNumber}
                                        </span>
                        </div>
                    </div>
                </Form.Group>
            </Fragment>
        )
    }

    /**
     * @description: Set initial state
     */
    useEffect(() => {
        document.title = userData.username + " | PayrollFile";
        if (userData !== Object(userData)) {
            setFormProfileContainer(<p className={"text-center font-size-25"}>Could not get the data</p>)
            // @ts-ignore
            setDate("0 days")
            return;
        }

        // @ts-ignore
        setDate(parseInt(String((
            new Date(userData.passwordExpiration).getTime()
            - new Date().getTime()) / (1000 * 60 * 60 * 24))) + " days");// date expiration

        let list = [];
        userData.authorities.map((authority) => {
            list.push(authority.roleCode + "_ ");
        });
        setAuthorities(<p className={"font-size-20 text-center"}> {list}</p>);

        /**
         * @description: Set form profile container
         */
        setFormProfileContainer(<FormProfile user={userData}/>);
    }, []);

    return (
        <Fragment>
            <div className={"form-main border-radius-main form-first box-shadow-main effect-main"}>
                <h1>Profile</h1>
                {formProfileContainer}
            </div>
            <div className={"form-div-content effect-main"}>
                <div className={"form-main border-radius-main form-second box-shadow-main"}>
                    <h5 className={"mt-4 ms-4"}>Password expires in: </h5>
                    <div className={"text-center font-size-30 "}>{date}</div>
                </div>
                <div className={"form-main border-radius-main form-second box-shadow-main"}>
                    <h5 className={"mt-4 ms-4"}>Assigned permissions: </h5>
                    <div className={"mt-2"}>
                        {authorities}
                    </div>
                </div>
            </div>
        </Fragment>
    )
}
export default Profile;