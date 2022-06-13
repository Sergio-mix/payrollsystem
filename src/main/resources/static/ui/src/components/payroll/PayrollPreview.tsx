import {Fragment, useEffect, useState} from "react";
import {MDBDataTableV5} from "mdbreact";

const PayrollPreview = (props) => {
    const [payroll, setPayroll] = useState(props.payroll);
    const [data, setData] = useState(props.data);
    const [content, setContent] = useState(null);

    const json = {
        columns: [{
            label: 'Name contributor',
            field: 'nameContributor',
            sort: 'asc',
            width: 150
        }, {
            label: 'Contributor document',
            field: 'documentNumberContributor',
            sort: 'asc',
            width: 150
        }, {
            label: 'Position',
            field: 'position',
            sort: 'asc',
            width: 150
        }, {
            label: 'Salary',
            field: 'salary',
            sort: 'asc',
            width: 150
        }, {
            label: 'Year and month',
            field: 'collaboratorDate',
            sort: 'asc',
            width: 150
        }, {
            label: 'Worked days',
            field: 'workedDays',
            sort: 'asc',
            width: 150
        }, {
            label: 'Days of disability',
            field: 'daysOfDisability',
            sort: 'asc',
            width: 150
        }, {
            label: 'Leave days',
            field: 'leaveDays',
            sort: 'asc',
            width: 150
        }, {
            label: 'Total days',
            field: 'totalDays',
            sort: 'asc',
            width: 150
        }, {
            label: 'Date of admission',
            field: 'dateOfAdmission',
            sort: 'asc',
            width: 150
        }, {
            label: 'Support',
            field: 'support',
            sort: 'asc',
            width: 150
        }, {
            label: 'Overtime hour',
            field: 'overtimeHour',
            sort: 'asc',
            width: 150
        }, {
            label: 'Over time hourFa',
            field: 'overtimeHourFa',
            sort: 'asc',
            width: 150
        }, {
            label: 'Commissions',
            field: 'commissions',
            sort: 'asc',
            width: 150
        }, {
            label: 'Holidays',
            field: 'holidays',
            sort: 'asc',
            width: 150
        }, {
            label: 'Required holiday',
            field: 'requiredHoliday',
            sort: 'asc',
            width: 150
        }, {
            label: 'Aj apor ins',
            field: 'ajAporIns',
            sort: 'asc',
            width: 150
        }, {
            label: 'Withdrawal bonus',
            field: 'withdrawalBonus',
            sort: 'asc',
            width: 150
        }, {
            label: 'Compensation',
            field: 'compensation',
            sort: 'asc',
            width: 150
        }, {
            label: 'Inability',
            field: 'inability',
            sort: 'asc',
            width: 150
        }]
    };

    const dataTable = (data) => {
        let list = [];
        data.map((item) => {
            item["documentNumberContributor"] = item.contributor.documentNumber;
            item["nameContributor"] = item.contributor.nameOfTheContributor;
            // @ts-ignore
            list.push(Object.assign({}, item, item.payrollDynamic));
        });

        json["rows"] = list;

        setContent(
            <MDBDataTableV5
                // @ts-ignore
                data={json}
                entriesOptions={[5, 10]}
                entries={5}
                searchTop
                searchBottom={false}
                responsive
                hover
            />
        );
    }

    useEffect(() => {
        dataTable(data);
    }, []);
    return (
        <Fragment>
            <div className={"form-second effect-main"}>
                <div className={"d-flex justify-content-center mb-4"}>
                    <h1>Payroll data</h1>
                </div>
                <nav
                    className={"table-responsive p-2 align-items-center bg-color border-radius-main box-shadow-main"}>
                    <ul className={"d-flex font-size-16 justify-content-between text-center"}>
                        <li className={"d-flex flex-column border-radius-main p-2"}>
                            <div className={"d-flex"}>
                                <span className={"font-size-18"}>TIPO DE DOCUMENTO</span>
                            </div>
                            <span
                                className={"font-size-18"}>{payroll.typeDocument.name}</span>
                        </li>
                        <li className={"d-flex flex-column border-radius-main p-2"}>
                            <div className={"d-flex"}>
                                <span className={"font-size-18"}>NUMERO</span>
                            </div>
                            <span
                                className={"font-size-18"}>{payroll.documentNumber}</span>
                        </li>
                        <li className={"d-flex flex-column border-radius-main p-2"}>
                            <div className={"d-flex"}>
                                <span className={"font-size-18"}>RAZON SOCIAL</span>
                            </div>
                            <span
                                className={"font-size-18"}>{payroll.businessName}</span>
                        </li>
                        <li className={"d-flex flex-column border-radius-main p-2"}>
                            <div className={"d-flex"}>
                                <span className={"font-size-20"}>REFERENCIA</span>
                            </div>
                            <span
                                className={"font-size-18"}>{payroll.reference}</span>
                        </li>
                        <li className={"d-flex flex-column border-radius-main p-2"}>
                            <div className={"d-flex"}>
                                <span className={"font-size-18"}>SOLICITUD</span>
                            </div>
                            <span
                                className={"font-size-18"}>{payroll.request}</span>
                        </li>
                    </ul>
                </nav>
                <div className={"center"}>
                    <div className="mt-4 form-main box-shadow-main border-radius-main p-4">
                        <h2>Data</h2>
                        {content}
                    </div>
                </div>
            </div>
        </Fragment>
    );
}

export default PayrollPreview;