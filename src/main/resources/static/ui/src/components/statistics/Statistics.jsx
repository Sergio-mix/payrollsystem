import {Fragment, useEffect} from "react";
import Chart from "react-apexcharts";

const Statistics = () => {
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

    useEffect(() => {
        document.title = "Statistics | PayrollFile";
    }, []);

    return (
        <Fragment>
            <div className="div-responsive effect-main">
                <div className="form-main form-aux-2 box-shadow-main border-radius-main">
                    <div className="card-header p-0 position-relative mt-n4 mx-3 bg-transparent">
                        <div className="py-3 pe-1">
                            <div className="chart">
                                <canvas className="chart-canvas"></canvas>
                            </div>
                        </div>
                    </div>
                    <div className="card-body">
                        <h6 className="mb-0 ">sws</h6>
                        <p className="text-sm ">Last Campaign Performance</p>
                        <hr className="dark horizontal"/>
                        <div className="d-flex ">
                            <p className="mb-0 text-sm"> campaign sent 2 days ago </p>
                        </div>
                    </div>
                </div>

                <div className="form-main form-aux-2 box-shadow-main border-radius-main">
                    <div className="card-header p-0 position-relative mt-n4 mx-3 bg-transparent">
                        <div className="py-3 pe-1">
                            <div className="chart">
                                <canvas className="chart-canvas"></canvas>
                            </div>
                        </div>
                    </div>
                    <div className="card-body">
                        <h6 className="mb-0 ">Website Views</h6>
                        <p className="text-sm ">Last Campaign Performance</p>
                        <hr className="dark horizontal"/>
                        <div className="d-flex ">
                            <p className="mb-0 text-sm"> campaign sent 2 days ago </p>
                        </div>
                    </div>
                </div>

                <div className="form-main form-aux-2 box-shadow-main border-radius-main">
                    <div className="card-header p-0 position-relative mt-n4 mx-3 bg-transparent">
                        <div className="py-3 pe-1">
                            <div className="chart">
                                <canvas className="chart-canvas"></canvas>
                            </div>
                        </div>
                    </div>
                    <div className="card-body">
                        <h6 className="mb-0 ">Website Views</h6>
                        <p className="text-sm ">Last Campaign Performance</p>
                        <hr className="dark horizontal"/>
                        <div className="d-flex ">
                            <p className="mb-0 text-sm"> campaign sent 2 days ago </p>
                        </div>
                    </div>
                </div>
            </div>
            <div className="form-main border-radius-main form-first box-shadow-main mb-5 effect-main">
                <h1>Demo</h1>
                <Chart
                    className=""
                    options={state.options}
                    series={state.series}
                    type="bar"
                    height={"350"}
                />
            </div>

            <div className="form-main border-radius-main form-aux box-shadow-main effect-main">
                <Chart options={state2.options} series={state2.series} type="radar" height={350} />
            </div>
        </Fragment>
    )
}

export default Statistics;