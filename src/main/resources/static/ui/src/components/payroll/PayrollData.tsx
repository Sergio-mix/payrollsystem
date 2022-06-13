import {Fragment, useEffect, useState} from "react"
import {useNavigate} from "react-router-dom";
import {MDBDataTableV5} from "mdbreact";
import {getPayrollAll, getPayrollDataByPayrollId} from "../../services/payrollService";
import {AiOutlineException} from "react-icons/all";
import {AiFillLock} from "react-icons/ai";
import PayrollPreview from "./PayrollPreview";

const PayrollData = (props) => {
    const navigate = useNavigate();

    const load = <div className={"text-center margin-5"}>
        <span className="loader"/>
    </div>;

    const [content, setContent] = useState(load);

    const error401 = () => {
        props.modal.openIsCloseNot(<div>
            <p className={"font-size-30 text-color-grey"}><AiFillLock/> Expired Session</p>
        </div>);

        setTimeout(() => {
            localStorage.clear();
            navigate("/login");
        }, 2000);
    }

    const query = async functionName =>
        await functionName.then(response => {
            return response;
        }).catch(error => {
            switch (error.response.status) {
                case 401:
                    error401();
                    break;
                case 404:
                    navigate("*");
                    break;
                case 500:
                    props.modal.open(
                        <div>
                            <h2>{error.response.data}</h2>
                        </div>);
                    break;
            }
        });

    const getPayrollData = async () => await query(getPayrollAll());
    const getPayrollDataById = async (payrollId) => await query(getPayrollDataByPayrollId(payrollId));

    const data = {
        columns: [{
            label: 'Document number',
            field: 'documentNumber',
            sort: 'asc',
            width: 150
        }, {
            label: 'Document number',
            field: 'typeDocument',
            sort: 'asc',
            width: 150
        }, {
            label: 'Reference',
            field: 'reference',
            sort: 'asc',
            width: 150
        }, {
            label: 'Request',
            field: 'request',
            sort: 'asc',
            width: 150
        }, {
            label: 'Business name',
            field: 'businessName',
            sort: 'asc',
            width: 150
        }, {
            label: 'Date',
            field: 'date',
            sort: 'asc',
            width: 150
        }, {
            label: 'Open payroll',
            field: 'PayrollData',
            sort: 'asc',
            width: 150
        }]
    };

    function formatDate(date) {
        let dateFormat = new Date(date);
        return dateFormat.getFullYear() + "-" + (dateFormat.getMonth() + 1)
            + "-" + dateFormat.getDate() + " " + dateFormat.getHours()
            + ":" + dateFormat.getMinutes() + ":" + dateFormat.getSeconds();
    }

    // @ts-ignore
    const openPayrollData = async (payroll) => {
        props.modal.openIsCloseNot(load);
        const response = await getPayrollDataById(payroll.id);
        switch (response.status) {
            case 200:
                props.modal.open(
                    <div className={"div-content-scroll row mh-500 text-justify mb-2"}>
                        <PayrollPreview data={response.data} payroll={payroll}/>
                    </div>
                );
                break;
        }

    }

    // @ts-ignore
    const getPayrollDataList = async () => {
        const response = await getPayrollData();
        let list = [];

        switch (response.status) {
            case 200:
                response.data.map(payroll => {
                    list.push({
                        documentNumber: payroll.documentNumber,
                        typeDocument: payroll.typeDocument.name,
                        reference: payroll.reference,
                        request: payroll.request,
                        businessName: payroll.businessName,
                        PayrollData: <button onClick={() => openPayrollData(payroll)}
                                             className="btn-2 bg-color box-shadow-main-2 text-color-grey border-radius-main">
                            <AiOutlineException className={"font-size-25"}/></button>,
                        date: formatDate(payroll.date),
                    });
                });

                data["rows"] = list
                break;
        }

        setContent(<MDBDataTableV5
            // @ts-ignore
            data={data}
            entriesOptions={[5, 10]}
            entries={5}
            searchTop
            searchBottom={false}
            responsive
            hover
        />);
    }

    useEffect(() => {
        getPayrollDataList().then(r => null);
    }, []);


    return (
        <Fragment>
            <div className={"form-main form-table border-radius-main box-shadow-main mb-5 effect-main"}>
                <div className="pb-0">
                    <h1>Payroll</h1>
                </div>
                <div className="">
                    {content}
                </div>
            </div>
        </Fragment>
    )
}

export default PayrollData