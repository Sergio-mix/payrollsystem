import {Fragment, useEffect, useState} from "react";
import Chart from "react-apexcharts";
import {AiFillLock} from "react-icons/ai";
import {getAvgSalary, getNumberContributors, getNumberPayrolls} from "../../services/payrollService";
import {useNavigate} from "react-router-dom";


const Statistics = (props) => {
    let state = {
        options: {
            chart: {
                id: "basic-bar"
            },
            xaxis: {
                categories: [1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998]
            }
        },
        series: [
            {
                name: "series-1",
                data: [30, 40, 45, 50, 49, 60, 70, 91]
            }
        ]
    };

    let state2 = {

        series: [{
            name: 'Series 1',
            data: [80, 50, 30, 40, 100, 20],
        }],
        options: {
            chart: {
                height: 350,
                type: 'radar',
            },
            title: {
                text: 'Basic Radar Chart'
            },
            xaxis: {
                categories: ['January', 'February', 'March', 'April', 'May', 'June']
            }
        },
    };

    const navigate = useNavigate();
    const [contributors, setContributors] = useState(0);
    const [payrolls, setPayrolls] = useState(0);
    const [avgSalary, setAvgSalary] = useState(0);

    const formatMoney = (cont) => {
        try {
            return cont != 0 ? cont.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,') : 0;
        } catch (e) {
            return cont;
        }
    }

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
            console.log(error.response.status);
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

    const getNumberContributorsStatistics = async () => await query(getNumberContributors());
    const getNumberPayrollsStatistics = async () => await query(getNumberPayrolls());
    const getAvgSalaryStatistics = async () => await query(getAvgSalary());

    // @ts-ignore
    const Statistics = async () => {
        const responseContributors = await getNumberContributorsStatistics();
        const responsePayrolls = await getNumberPayrollsStatistics();
        const responseAvgSalary = await getAvgSalaryStatistics();

        switch (responseContributors.status) {
            case 200:
                setContributors(responseContributors.data);
                break;
        }

        switch (responsePayrolls.status) {
            case 200:
                setPayrolls(responsePayrolls.data);
                break;
        }

        switch (responseAvgSalary.status) {
            case 200:
                setAvgSalary(responseAvgSalary.data);
                break;
        }
    }

    useEffect(() => {
        Statistics().then(r => null);
        document.title = "Statistics | Payroll";
    }, []);

    return (
        <Fragment>
            <div className="div-responsive effect-main">
                <div className="form-main form-aux-2 box-shadow-main border-radius-main text-center">
                    <div className="card-header p-0 position-relative mt-n4 mx-3 bg-transparent">
                        <div className="py-3 pe-1">
                            <div className="chart">
                                <h1 className={"font-size-50 chart-canvas text-color-aux"}>{contributors}</h1>
                            </div>
                        </div>
                    </div>
                    <div className="card-body">
                        <h6 className="mb-0 ">Number of contributors registered in the system</h6>
                        <hr className="dark horizontal"/>
                        <div className="d-flex ">
                            <p className="mb-0 text-sm"> {Date()} </p>
                        </div>
                    </div>
                </div>

                <div className="form-main form-aux-2 box-shadow-main border-radius-main text-center">
                    <div className="card-header p-0 position-relative mt-n4 mx-3 bg-transparent">
                        <div className="py-3 pe-1">
                            <div className="chart">
                                <h1 className={"font-size-50 chart-canvas text-color-aux"}>{payrolls}</h1>
                            </div>
                        </div>
                    </div>
                    <div className="card-body">
                        <h6 className="mb-0 ">Number of Payrolls registered in the system</h6>
                        <hr className="dark horizontal"/>
                        <div className="d-flex">
                            <p className="mb-0 text-sm"> {Date()} </p>
                        </div>
                    </div>
                </div>

                <div className="form-main form-aux-2 box-shadow-main border-radius-main text-center">
                    <div className="card-header p-0 position-relative mt-n4 mx-3 bg-transparent">
                        <div className="py-3 pe-1">
                            <div className="chart">
                                <h1 className={"font-size-50 chart-canvas text-color-aux"}>{formatMoney(avgSalary)}</h1>
                            </div>
                        </div>
                    </div>
                    <div className="card-body">
                        <h6 className="mb-0 ">Average salary of employees</h6>
                        <hr className="dark horizontal"/>
                        <div className="d-flex">
                            <p className="mb-0 text-sm"> {Date()} </p>
                        </div>
                    </div>
                </div>
            </div>
            {/*<div className="form-main border-radius-main form-first box-shadow-main mb-5 effect-main">*/}
            {/*    <h1>Demo</h1>*/}
            {/*<Chart*/}
            {/*    className=""*/}
            {/*    options={state.options}*/}
            {/*    series={state.series}*/}
            {/*    type="bar"*/}
            {/*    height={"350"}*/}
            {/*/>*/}
            {/*</div>*/}

            {/*<div className="form-main border-radius-main form-aux box-shadow-main effect-main">*/}
            {/*    /!*<Chart options={state2.options} series={state2.series} type="radar" height={350}/>*!/*/}
            {/*</div>*/}
        </Fragment>
    )
}

export default Statistics;