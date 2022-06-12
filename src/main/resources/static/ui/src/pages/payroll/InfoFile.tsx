import {Fragment, useEffect, useState} from "react"
import {useLocation} from "react-router-dom";

const InfoFile = () => {
    const location = useLocation();
    // @ts-ignore
    const [data, setData] = useState(location.state.data);
    const [infoFile, setInfoFile] = useState(null);
    const [headersData, setHeadersData] = useState(null);
    const [itemsData, setItemsData] = useState(null);
    const [problems, setProblems] = useState(null);

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
            if (item.attribute === attribute) {
                problems.push(item);
            }
        });
        listItemsProblems(problems);
    }

    function ItemHerderData(props) {

        return (
            <Fragment>
                <p>{props.data}</p>
            </Fragment>
        )
    }

    function ItemData(props) {
        const [subItems, setSubItems] = useState([]);
        let list = [];

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
        ];

        useEffect(() => {
            return () => {
                jsonData.map((item, index) => {
                    let value = props.data[item.item];
                    list.push(
                        <div className={"center m-2 p-2 cursor-pointer"} onClick={() => props.event(props.data)}>
                            <p className={"font-size-18"}>{value != null ? value : "Not"}</p>
                        </div>
                    );
                })
                setSubItems(list);
            }
        }, []);
        return (
            <Fragment>
                <div
                    className={"d-flex m-auto center justify-content-between border-radius-main box-shadow-hover p-2 m-2"}>
                    {subItems}
                </div>
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
                <h1>{formatTitle(props.data.attribute)}</h1>
                <p>{props.data.message}</p>
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

    const listItemsData = (data) => {
        let items = [];
        data.map((item, index) => {
            items.push(<ItemData data={item} key={index} event={searchProblemsData}/>)
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
        // @ts-ignore
        console.log(data);
        // @ts-ignore
        listHeaders(data.headersData);
        // @ts-ignore
        listItemsData(data.payrollFileData);
    }, []);

    return (
        <Fragment>
            <div className={"m-5"}>
                <div className={"form-main form-second box-shadow-main border-radius-main effect-main"}>
                    <div className={"justify-content-between d-flex"}>
                        <div className={"d-flex flex-column cursor-pointer"}
                             onClick={() => searchProblems(data, "typeDocument")}>
                            <span className={"font-size-20"}>TIPO DE DOCUMENTO</span>
                            <span
                                className={"font-size-18"}>{data.typeDocument != null && data.typeDocument != "" ? data.typeDocument : "Not value"}</span>
                        </div>
                        <div className={"d-flex flex-column cursor-pointer"}
                             onClick={() => searchProblems(data, "documentNumber")}>
                            <span className={"font-size-20"}>NUMERO</span>
                            <span
                                className={"font-size-18"}>{data.documentNumber != null && data.documentNumber != "" ? data.documentNumber : "Not value"}</span>
                        </div>
                        <div className={"d-flex flex-column cursor-pointer"}
                             onClick={() => searchProblems(data, "businessName")}>
                            <span className={"font-size-20"}>RAZON SOCIAL</span>
                            <span
                                className={"font-size-18"}>{data.businessName != null && data.businessName != "" ? data.businessName : "Not value"}</span>
                        </div>
                        <div className={"d-flex flex-column cursor-pointer"}
                             onClick={() => searchProblems(data, "reference")}>
                            <span className={"font-size-20"}>REFERENCIA</span>
                            <span
                                className={"font-size-18"}>{data.reference != null && data.reference != "" ? data.reference : "Not value"}</span>
                        </div>
                        <div className={"d-flex flex-column cursor-pointer"}
                             onClick={() => searchProblems(data, "request")}>
                            <span className={"font-size-20"}>SOLICITUD</span>
                            <span
                                className={"font-size-18"}>{data.request != null && data.request != "" ? data.request : "Not value"}</span>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div
                        className={"mt-4 form-main form-input box-shadow-main effect-main border-radius-main"}>
                        <div className={"d-flex box-shadow-main-2 border-radius-main p-2 mb-2 text-center center"}>
                            {headersData}
                        </div>
                        {itemsData}
                    </div>
                    <div className={"form-main form-aux box-shadow-main border-radius-main effect-main"}>
                        {problems}
                    </div>
                </div>

            </div>
        </Fragment>
    )
}

export default InfoFile;