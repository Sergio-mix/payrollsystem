import {Fragment, useEffect, useState} from "react";
import {enableUser, getUsersAll} from "../../services/userService";
import {MDBDataTableV5} from 'mdbreact';
import {AiOutlineReload} from "react-icons/ai";
import UserForm from "./UserForm";
import {useNavigate} from "react-router-dom";

/**
 * @description Component that displays the list of users
 */
const Users = (props) => {

    const load = <div className={"text-center margin-5"}>
        <span className="loader"/>
    </div>;

    const [content, setContent] = useState(contentTable(load));

    const REGISTER = "register";
    const UPDATE = "update";

    const navigate = useNavigate();

    let users = [];

    function setUsers(list) {
        users = list;
    }

    /**
     * @description set the content of the table
     * @param content the content of the table
     */
    function contentTable(content) {
        return <div className={"form-main form-table border-radius-main box-shadow-main mb-5 effect-main"}>
            <div className="pb-0">
                <h1>Users</h1>
            </div>
            <div className="">
                {content}
            </div>
        </div>
    }

    // get the users details
    const updateUser = (user) => {
        setContent(<UserForm userData={{title: "Update user", titleBtn: "Update", action: UPDATE, user: user}}
                             close={listUsersHome} closeOk={refreshAllUsers} modal={props.modal}/>);
    };

    // get the users register
    const registerUser = () => {
        setContent(<UserForm userData={{title: "Register user", titleBtn: "Register", action: REGISTER}}
                             close={listUsersHome} closeOk={refreshAllUsers} modal={props.modal}/>);
    }

    // open user update
    const userBtnUpdate = (user) => {
        return <button className={"btn-2 bg-color box-shadow-main-2 text-color-grey border-radius-main"}
                       onClick={ev => updateUser(user)}>Update</button>
    };

    /**
     * @description change a user's status
     * @param id the user's id
     */
    const userDisabled = async (id) =>
        await enableUser(id).then(rest => {
            if (rest.status === 200) {
                users.forEach(user => {
                    if (user.id === id) {
                        user.enabled = rest.data.enabled;
                        if (user.enabled) {
                            document.getElementById(id + "enable").classList.remove("text-color-red");
                            document.getElementById(id + "enable").classList.add("text-color-green");
                        } else {
                            document.getElementById(id + "enable").classList.remove("text-color-green");
                            document.getElementById(id + "enable").classList.add("text-color-red");
                        }

                        document.getElementById(id + "enable").innerHTML = user.enabled == true ? "Active" : "Inactive";
                    }
                });

                props.modal.open(
                    <div>
                        <h1>Status update</h1>
                        <p className={"font-size-25 text-color-grey"}>{rest.data.message}</p>
                    </div>
                );
            }
        }).catch(err => {
            props.modal.open(
                <div>
                    <h1 className={"text-color-red"}>:(</h1>
                    <p>{err.data}</p>
                </div>
            );
        });

    /**
     * @description all the users
     */
    const allUser = async () =>
        await getUsersAll().then(response => {
            if (response.data === undefined) navigate("/login");
            return response.data;
        }).catch(error => {
            switch (error.response.status) {
                case 401:
                    navigate("/401");
                    break;
                case 404:
                    navigate("*");
                    break;
                case 500:
                    setContent(contentTable(<p className={"text-center font-size-25"}>{error.response.data}</p>));
                    break;
            }
        });

    const allUsers = async () => setUsers(allInsertTable(await allUser()));

    /**
     * @description refresh users status
     * @param user the user's status
     */
    const userStatus = (user) => {
        let color = "text-color-green";
        let text = "Active";

        if (Number(localStorage.getItem("USER_ID")) === user.id)
            return <label className={"text-color-green font-size-16"}>Active</label>

        if (!user.enabled) {
            color = "text-color-red";
            text = "Inactive";
        }

        return <button
            id={user.id + "enable"}
            className={"btn-2 bg-color box-shadow-main-2 " + color + " border-radius-main"}
            onClick={ev => {
                userDisabled(user.id), props.modal.openIsCloseNot(
                    <div>
                        <h1>Status update</h1>
                        {load}
                    </div>
                );
            }//set event

            }>{text}</button>
    };

    /**
     * @description insert the table
     * @param list the list of users
     */
    const allInsertTable = (list) => {
        if (list.length === 0)
            return setContent(contentTable(<p className={"text-center font-size-25"}>There are no registered
                users</p>));

        const data = {
            columns: [{
                label: 'Username',
                field: 'username',
                sort: 'asc',
                width: 150
            }, {
                label: 'Names',
                field: 'names',
                sort: 'asc',
                width: 150
            }, {
                label: 'Last names',
                field: 'lastNames',
                sort: 'asc',
                width: 150
            }, {
                label: 'CellPhone',
                field: 'cellPhone_country_number',
                sort: 'asc',
                width: 150
            }, {
                label: 'Email',
                field: 'email',
                sort: 'asc',
                width: 150
            }, {
                label: 'Document number',
                field: 'document_type_number',
                sort: 'asc',
                width: 150
            }, {
                label: '#',
                field: 'status',
                sort: 'asc',
                width: 150
            }, {
                label: '#',
                field: 'action',
                sort: 'asc',
                width: 150
            }]
        };

        list.forEach(user => {
            user["document_type_number"] = "(" + user.typeDocument.name + ") " + user.documentNumber;
            user["cellPhone_country_number"] = "+(" + user.countryCode.code + ") " + user.cellPhone;
            user["status"] = userStatus(user);
            user["action"] = userBtnUpdate(user);
        });

        data["rows"] = list;

        setContent(
            contentTable(
                <Fragment>
                    <button onClick={registerUser}
                            className={"btn-3-user border-radius-main box-shadow-main-2 mb-2"}>Register
                        user
                    </button>
                    <MDBDataTableV5
                        // @ts-ignore
                        data={data}
                        entriesOptions={[5, 10]}
                        entries={5}
                        searchTop
                        searchBottom={false}
                        responsive
                        hover
                    />
                    <div onClick={refreshAllUsers} className={"text-color-grey-hover-aux"}>
                        <AiOutlineReload className={"ms-3 font-size-25"}/>
                        <span className={"ms-2 font-size-16"}>Reload data</span>
                    </div>
                </Fragment>
            ));

        return list;
    }

    // get the users
    const listUsersHome = () => {
        setContent(contentTable(load));
        allInsertTable(users);
    }

    // get the users refresh
    const refreshAllUsers = () => {
        setContent(contentTable(load));
        allUsers().then(() => null);
    }

    /**
     * @description: Set initial state
     */
    useEffect(() => {
        document.title = "Users | PayrollFile";
        allUsers().then(r => null);
    }, []);

    return (
        <Fragment>
            {content}
        </Fragment>
    )
}

export default Users;