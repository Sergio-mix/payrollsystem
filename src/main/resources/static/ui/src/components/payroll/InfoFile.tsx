import {Fragment, useEffect, useState} from "react"

const InfoFile = (props) => {
    // @ts-ignore
    const [data, setData] = useState(props.data);
    const [headersData, setHeadersData] = useState(null);
    const [itemsData, setItemsData] = useState(null);
    const [problems, setProblems] = useState([]);
    let [cont, setCont] = useState("(o)");

    const jsonData = [
        {item: "order"},
        {item: "typeDocument"},
        {item: "documentNumber"},
        {item: "nameOfTheContributor"},
        {item: "position"},
        {item: "year"},
        {item: "month"},
        {item: "salary"},
        {item: "workedDays"},
        {item: "daysOfDisability"},
        {item: "leaveDays"},
        {item: "totalDays"},
        {item: "dateOfAdmission"},
        {item: "minimumWage"},
        {item: "support"},
        {item: "overtimeHour"},
        {item: "overtimeHourFa"},
        {item: "commissions"},
        {item: "holidays"},
        {item: "requiredHoliday"},
        {item: "ajAporIns"},
        {item: "withdrawalBonus"},
        {item: "compensation"},
        {item: "inability"},
    ];

    const searchProblemsData = (item) => {
        let problems = [];
        item.validateErrors.filter(item => {
            problems.push(item);
        });
        listItemsProblems(problems);
    }

    const searchProblems = (item, attribute) => {
        let problems = [];
        item.validateErrors.filter(item => {
            if (attribute.indexOf(item.attribute) !== -1) {
                problems.push(item);
            }
        });
        listItemsProblems(problems);
    }

    const searchProblemsSize = (item, attribute) => {
        let problems = 0;
        item.validateErrors.filter(item => {
            if (attribute.indexOf(item.attribute) !== -1) {
                problems++;
            }
        });
        let message = "(" + problems + ")"
        return problems > 0 ? message : null;
    }

    function ItemHerderData(props) {

        return (
            <Fragment>
                <th className={"text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"}>
                    {props.data != null && props.data != "" ? props.data :
                        <p className={"text-color-aux"}>###</p>}</th>
            </Fragment>
        )
    }

    function ItemData(props) {
        const [subItems, setSubItems] = useState([]);
        let list = [];

        // @ts-ignore
        let data = Object.assign({}, props.data, props.data.dynamicData);

        useEffect(() => {
                props.json.map((item, index) => {
                    let value = data[item.item];
                    list.push(
                        <td className={"cursor-pointer font-size-16"} onClick={() => props.event(props.data)}>
                            {value != null && value != "" || value == 0 ? value :
                                <p className={"text-color-yellow"}>###</p>}
                        </td>
                    );
                })
                setSubItems(list);
        }, []);
        return (
            <Fragment>
                <tr
                    className={"box-shadow-hover-aux text-center border-radius-main"}>
                    {subItems}
                </tr>
            </Fragment>
        )
    }

    function ItemProblem(props) {

        function formatTitle(title) {
            let st = "";
            for (let stTitle of title) {
                st += stTitle.toUpperCase() === stTitle ? " " + stTitle.toLowerCase() : stTitle;
            }
            return st;
        }

        return (
            <Fragment>
                <h2 className={"font-size-20"}><span
                    className={"text-color-aux"}>- </span>{formatTitle(props.data.attribute)}</h2>
                <p className={"font-size-18"}>{props.data.message}</p>
            </Fragment>
        )
    }

    const listHeaders = (data) => {
        let items = [];
        data.map((item, index) => {
            items.push(<ItemHerderData data={item} key={index}/>)
        })
        setHeadersData(items);
    }

    const listItemsData = (data, json) => {
        let items = [];
        data.map((item, index) => {
            items.push(<ItemData data={item} json={json} key={index} event={searchProblemsData}/>)
        })
        setItemsData(items);
    }

    const listItemsProblems = (data) => {
        let items = [];
        data.map((item, index) => {
            items.push(<ItemProblem data={item} key={index}/>)
        })
        setProblems(items);
    }

    useEffect(() => {
        listHeaders([...data.headersData, ...data.headersDataDynamic]);
        listItemsData(data.payrollFileData != null ? data.payrollFileData : [], jsonData);

        let n = 0;
        if (data.payrollFileData !== null) {
            data.payrollFileData.map(item => {
                n += item.validateErrors.length
            });
            let message = "(" + n + ")"
            setCont(n > 0 ? message : null);
        }
    }, []);

    return (
        <Fragment>
            <div className={"form-second effect-main"}>
                <div className={"d-flex justify-content-center mb-4"}>
                    <h1>Payroll inconsistencies</h1>
                </div>
                <nav
                    className={"table-responsive p-2 align-items-center bg-color border-radius-main box-shadow-main"}>
                    <ul className={"d-flex font-size-20 justify-content-between text-center"}>
                        <li className={"d-flex flex-column cursor-pointer box-shadow-hover border-radius-main p-2"}
                            onClick={() => searchProblems(data, ["typeDocument"])}>
                            <div className={"d-flex"}>
                                <span className={"font-size-20"}>TIPO DE DOCUMENTO</span>
                                <p className={"text-color-aux ms-1 font-size-16"}>{searchProblemsSize(data, ["typeDocument"])}</p>
                            </div>
                            <span
                                className={"font-size-18"}>{data.typeDocument != null && data.typeDocument != "" || data.typeDocument == 0 ? data.typeDocument :
                                <p className={"text-color-aux"}>###</p>}</span>
                        </li>
                        <li className={"d-flex flex-column cursor-pointer box-shadow-hover border-radius-main p-2"}
                            onClick={() => searchProblems(data, ["documentNumber"])}>
                            <div className={"d-flex"}>
                                <span className={"font-size-20"}>NUMERO</span>
                                <p className={"text-color-aux ms-1 font-size-16"}>{searchProblemsSize(data, ["documentNumber"])}</p>
                            </div>
                            <span
                                className={"font-size-18"}>{data.documentNumber != null && data.documentNumber != "" || data.documentNumber == 0 ? data.documentNumber :
                                <p className={"text-color-aux"}>###</p>}</span>
                        </li>
                        <li className={"d-flex flex-column cursor-pointer box-shadow-hover border-radius-main p-2"}
                            onClick={() => searchProblems(data, ["businessName"])}>
                            <div className={"d-flex"}>
                                <span className={"font-size-20"}>RAZON SOCIAL</span>
                                <p className={"text-color-aux ms-1 font-size-16"}>{searchProblemsSize(data, ["businessName"])}</p>
                            </div>
                            <span
                                className={"font-size-18"}>{data.businessName != null && data.businessName != "" || data.businessName == 0 ? data.businessName :
                                <p className={"text-color-aux"}>###</p>}</span>
                        </li>
                        <li className={"d-flex flex-column cursor-pointer box-shadow-hover border-radius-main p-2"}
                            onClick={() => searchProblems(data, ["reference"])}>
                            <div className={"d-flex"}>
                                <span className={"font-size-20"}>REFERENCIA</span>
                                <p className={"text-color-aux ms-1 font-size-16"}>{searchProblemsSize(data, ["reference"])}</p>
                            </div>
                            <span
                                className={"font-size-18"}>{data.reference != null && data.reference != "" || data.reference == 0 ? data.reference :
                                <p className={"text-color-aux"}>###</p>}</span>
                        </li>
                        <li className={"d-flex flex-column cursor-pointer box-shadow-hover border-radius-main p-2"}
                            onClick={() => searchProblems(data, ["request"])}>
                            <div className={"d-flex"}>
                                <span className={"font-size-20"}>SOLICITUD</span>
                                <p className={"text-color-aux ms-1 font-size-16"}>{searchProblemsSize(data, ["request"])}</p>
                            </div>
                            <span
                                className={"font-size-18"}>{data.request != null && data.request != "" || data.request == 0 ? data.request :
                                <p className={"text-color-aux"}>###</p>}</span>

                        </li>
                    </ul>
                </nav>
                <div className="row mt-4">
                    <div
                        className="table-responsive p-0 form-main form-first border-radius-main box-shadow-main p-5 div-content-scroll mh-500">
                        <h2>Data: <span
                            className={"font-size-25"}>{data.payrollFileData != null ? data.payrollFileData.length : 0}</span>
                            <span className={"text-color-aux ms-1 font-size-18"}>{cont}</span>
                        </h2>
                        <table
                            className={"table align-items-center"}>
                            <thead>
                            <div>
                                <p className={"text-color-aux ms-1 font-size-18"}>{searchProblemsSize(data, ["headersData", "headersDataDynamic"])} </p>
                            </div>
                            <tr onClick={() =>
                                searchProblems(data, ["headersData", "headersDataDynamic"])}>
                                {headersData}
                            </tr>
                            {itemsData}
                            </thead>
                        </table>
                    </div>
                    <div className={"form-main form-aux box-shadow-main border-radius-main div-content-scroll mh-350"}>
                        <h2>Inconsistencies</h2>
                        {problems}
                    </div>
                </div>
            </div>
        </Fragment>
    )
}

export default InfoFile;