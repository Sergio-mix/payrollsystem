import {Fragment, useEffect, useState} from "react";
import {MDBDataTableV5} from "mdbreact";
import {
    getTraceabilityAll, getTraceabilityByDate, getTraceabilityByDateUser,
    getTraceabilityByUserUserId
} from "../../services/TraceabilityService";
import Select from "react-select";
import {AiFillLock, AiOutlineFullscreen, AiOutlineSearch, AiOutlineSolution} from "react-icons/ai";
import {getUsersAllNames} from "../../services/userService";
import {useNavigate} from "react-router-dom";

/**
 * @description: Component that displays the traceability table
 */
const Traceability = (props) => {

    const load = <div className={"text-center margin-5"}>
        <span className="loader"/>
    </div>;//loader

    const [contentUser, setContentUser] = useState(null);
    const [contentTable, setContentTable] = useState(load);
    const [loading, setLoading] = useState(true);
    const [inputs, setInputs] = useState(null);

    const navigate = useNavigate();

    let beginning = null;
    let finalDate = null;
    let searchUserDate = null;

    const setBeginning = value => {//set the beginning date
        beginning = formatDate(value)
    };

    const setFinalDate = value => {//set the final date
        finalDate = formatDate(value);
    };

    const setSearchUserDate = value => {//set the user date
        searchUserDate = value.value;
    };

    /**
     * @description: Loads the data for the table
     * @param functionName the function to call
     */
    const query = async functionName =>
        await functionName.then(response => {
            return response.data;
        }).catch(error => {
            switch (error.response.status) {
                case 400:
                    setContentTable(<p
                        className={"text-center font-size-25"}>{error.response.data}</p>);
                    break;
                case 401:
                    props.modal.openIsCloseNot(<div>
                        <p className={"font-size-30 text-color-grey"}><AiFillLock/> Expired Session</p>
                    </div>);

                    setTimeout(() => {
                        localStorage.clear();
                        navigate("/login");
                    }, 2000);
                    break;
                case 404:
                    navigate("*");
                    break;
                case 500:
                    setContentTable(<p
                        className={"text-center font-size-25"}>{error.response.data}</p>);
                    break;
            }
        });


    const allUserNames = async () => await query(getUsersAllNames());//get all users by names

    const searchDate = async (beginning, finalDate) => traceabilityTable(await query(getTraceabilityByDate(beginning, finalDate)), allData);//search by date

    const searchDateAndUser = async (beginning, finalDate, id) => traceabilityTable(await query(getTraceabilityByDateUser(beginning, finalDate, id)), userData);//search by date and user

    const searchUser = async (id) => traceabilityTable(await query(getTraceabilityByUserUserId(id)), userData);//search by user

    const allTraceabilityList = async () => traceabilityTable(await query(getTraceabilityAll()), allData);//get all traceability

    const allData = {
        columns: [{
            label: 'Username',
            field: 'username',
            sort: 'asc',
            width: 150
        }, {
            label: 'Record name',
            field: 'record_name',
            sort: 'asc',
            width: 150
        }, {
            label: 'Date',
            field: 'date',
            sort: 'asc',
            width: 150
        }, {
            label: 'Ip',
            field: 'ip',
            sort: 'asc',
            width: 150
        }, {
            label: '#',
            field: 'action',
            sort: 'asc',
            width: 150
        }]
    };

    const userData = {
        columns: [{
            label: 'Record name',
            field: 'record_name',
            sort: 'asc',
            width: 150
        }, {
            label: 'Date',
            field: 'date',
            sort: 'asc',
            width: 150
        }, {
            label: 'Ip',
            field: 'ip',
            sort: 'asc',
            width: 150
        }, {
            label: '#',
            field: 'action',
            sort: 'asc',
            width: 150
        }]
    };

    /**
     * @description: Detailed information about the traceability
     * @param props
     */
    function UserForm(props) {
        document.getElementById("form-table").classList.replace("form-table", "form-first");//update the class of the form

        let descriptionRecord = null

        if (props.record.description !== null) {//if description is not null add it to the description
            descriptionRecord = <span className={"text-input"}><h4>Description </h4> {props.record.description}</span>;
        }

        let list = [];
        props.record.user.authorities.map((authority) => {//add the authorities to the list
            list.push(authority.roleCode + "_ ");
        });

        //add the information to the table
        return (
            <Fragment>
                <div className={"form-main border-radius-main form-aux box-shadow-main effect-main"}>
                    <h2>{props.record.user.username}</h2>
                    <div className={"form-group"}>
                        <span className={"text-input"}><b>Names: </b> {props.record.user.names}</span>
                        <span className={"text-input"}><b>Last names: </b> {props.record.user.lastNames}</span>
                        <span className={"text-input"}><b>Email: </b> {props.record.user.email}</span>
                        <span
                            className={"text-input"}><b>Cell Phone: </b> {"(+" + props.record.user.countryCode.code + ") " + props.record.user.cellPhone}</span>
                        <span
                            className={"text-input"}><b>Document: </b> {"(" + props.record.user.typeDocument.name + ") " + props.record.user.documentNumber}</span>
                        <span className={"text-input"}><b>Authorities: </b> {list}</span>
                        <h4>Data </h4>
                        <span className={"text-input"}><b>Record name: </b> {props.record.record.name}</span>
                        <span className={"text-input"}><b>Date: </b> {props.record.date}</span>
                        <span className={"text-input"}><b>Ip: </b> {props.record.ip}</span>
                        {descriptionRecord}
                    </div>
                </div>
            </Fragment>
        );
    }

    /**
     * @description: insert the data in the table
     * @param list the list of records
     * @param data the data of the table
     */
    function traceabilityTable(list, data) {
        if (list.length === 0)//if the list is empty
            return setContentTable(<p className={"text-center font-size-25"}>No records found</p>);

        let listRecord = [];

        list.forEach(record => {
            record["username"] = record.user.username;
            record["date"] = formatDate(record["date"]);
            record["record_name"] = record.record.name;
            record["action"] = <button className={"btn-2 border-radius-main box-shadow-main-2 bg-color text-color-aux"}
                                       onClick={() => setContentUser(<UserForm record={record}/>)}><AiOutlineSolution
                className={"font-size-20"}/></button>;

            listRecord.push(record);
        });

        data["rows"] = listRecord;

        //add the information to the table
        setContentTable(
            <Fragment>
                <MDBDataTableV5
                    small
                    // @ts-ignore
                    data={data}//data of the table
                    entriesOptions={[5, 10]}
                    entries={5}
                    searching={false}
                    responsive
                    hover

                />
            </Fragment>
        );

        return list;
    }

    /**
     * @description: format the date
     * @param date {string} the date
     */
    function formatDate(date) {
        let dateFormat = new Date(date);
        return dateFormat.getFullYear() + "-" + (dateFormat.getMonth() + 1)
            + "-" + dateFormat.getDate() + " " + dateFormat.getHours()
            + ":" + dateFormat.getMinutes() + ":" + dateFormat.getSeconds();
    }

    function SelectUser(props) {
        let options = [];

        allUserNames().then(r => {
            r.forEach(user => {
                options.push({value: user.idUser, label: user.username});
            });
        });

        return (
            <Fragment>
                <Select
                    className={"w-25"}
                    options={options}
                    isLoading={props.loading}
                    placeholder={"Select user name..."}
                    onChange={props.fun}
                />
            </Fragment>
        )
    }

    function SelectDate(props) {
        return (
            <Fragment>
                <input className={"input w-30 p-2 m-1"} type={"datetime-local"}
                       onChange={(event: any) => setBeginning(event.target.value)}/>
                <input className={"input w-30 p-2 m-1"} type={"datetime-local"}
                       max={formatDate(new Date())}
                       onChange={(event: any) => setFinalDate(event.target.value)}/>
                <span onClick={props.fun}><AiOutlineSearch
                    className={"icon-search"}/></span>
            </Fragment>
        )
    }

    /**
     * @description: Detailed information about the traceability
     * @param value {string} the value of the search
     */
        // @ts-ignore
    const handleChange = async (value) => {
            setLoading(true);//set loading
            //type of the search
            switch (value.value) {
                case "all":
                    setInputs(null);
                    setContentUser(null);
                    document.getElementById("form-table").classList.remove("form-first");
                    document.getElementById("form-table").classList.add("form-table");
                    setContentTable(load);
                    allTraceabilityList().then(r => setLoading(false));//get all the traceability
                    break;
                case "user":
                    setInputs(<SelectUser fun={searchUserOnClick} loading={loading}/>);
                    break;
                case "date":
                    setInputs(null);
                    setInputs(<SelectDate fun={searchDateOnClick}/>);
                    break;
                case "dateAndUser":
                    setInputs(
                        <Fragment>
                            <SelectUser fun={setSearchUserDate} loading={loading}/>
                            <SelectDate fun={searchUserDateOnClick}/>
                        </Fragment>
                    );
                    break;
            }
        }

    /**
     * @description: Search the traceability by username
     * @param id {number} the id of the user
     */
    function searchUserOnClick(id) {
        setContentUser(null);
        document.getElementById("form-table").classList.remove("form-first");
        document.getElementById("form-table").classList.add("form-table");
        setContentTable(load);
        searchUser(id.value).then(r => setLoading(false));//get the traceability by username
    }

    /**
     * @description: Search the traceability by date
     */
    function searchDateOnClick() {
        if (beginning >= finalDate) {
            setContentTable(<p
                className={"text-center font-size-25"}>{"The beginning formatDate must be before the final formatDate"}</p>);
            return;
        }

        if (finalDate > formatDate(new Date())) {
            setContentTable(<p
                className={"text-center font-size-25"}>{"The end formatDate cannot be after today"}</p>);
            return;
        }

        setContentUser(null);
        document.getElementById("form-table").classList.remove("form-first");
        document.getElementById("form-table").classList.add("form-table");
        setContentTable(load);

        searchDate(beginning, finalDate).then(r => setLoading(false));// search by date
    }

    /**
     * @description: Search the traceability by user name and date
     */
    function searchUserDateOnClick() {
        if (beginning === null || finalDate === null || searchUserDate === null) {
            setContentTable(<p
                className={"text-center font-size-25"}>{"Select a formatDate"}</p>);
            return;
        }

        if (beginning >= finalDate) {
            setContentTable(<p
                className={"text-center font-size-25"}>{"The beginning formatDate must be before the final formatDate"}</p>);
            return;
        }

        if (finalDate > formatDate(new Date())) {
            setContentTable(<p
                className={"text-center font-size-25"}>{"The end formatDate cannot be after today"}</p>);
            return;
        }

        setContentUser(null);
        document.getElementById("form-table").classList.remove("form-first");
        document.getElementById("form-table").classList.add("form-table");
        setContentTable(load);

        searchDateAndUser(beginning, finalDate, searchUserDate).then(r => setLoading(false));//search by date and user
    }

    /**
     * @description: Set initial state
     */
    useEffect(() => {
        document.title = "Traceability | Payroll";
        allTraceabilityList().then(r => setLoading(false));
    }, []);

    return (
        <Fragment>
            <div id={"form-table"}
                 className={"form-main border-radius-main form-table box-shadow-main mb-5 effect-main"}>
                <h1>Users traceability</h1>
                <div className={"mt-4"}>
                    <div className={"mb-3 content-item-search text-center"}>
                        {inputs}
                    </div>
                    {contentTable}
                    <div className={"d-flex mb-4"}>
                        <Select
                            className={"wls mt-2 select border-radius-main"}
                            options={[{value: "all", label: "All"}, {value: "date", label: "Date"}, {
                                value: "user",
                                label: "User"
                            }, {
                                value: "dateAndUser",
                                label: "Date and user"
                            }]}
                            defaultValue={[{value: "all", label: "All"}]}
                            isLoading={loading}
                            placeholder={"Select..."}
                            onChange={handleChange}
                        />
                    </div>
                </div>
            </div>
            {contentUser}
        </Fragment>
    )
}

export default Traceability;