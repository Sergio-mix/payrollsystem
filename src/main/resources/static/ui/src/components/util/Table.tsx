import { Fragment } from "react";


const Table = (props) => {
    let add = [];

    return (
        <Fragment>
            <div className="container">
                <div className="col-12">
                    <div className="content mb-4">
                        <div className="card-header pb-0">
                            <h6>{props.name}</h6>
                        </div>
                        <div>
                            {add}
                        </div>
                        <div className="card-body px-0 pt-0 pb-2">
                            <div className="table-responsive p-0">
                                <table className="table align-items-center mb-0">
                                    <thead>
                                    <tr>
                                        {props.columns.map((col, index) => (
                                            <th className="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
                                                key={index} scope="col">{col.column}</th>
                                        ))}

                                        <th className="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
                                            scope="col">#
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {
                                        props.data.length > 0 && props.data.map((item, index) => (
                                            <tr key={index}>
                                                {props.columns.map((col, index) => (
                                                    <td key={index}>{item[col.value]}</td>
                                                ))}
                                            </tr>
                                        ))
                                    }
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </Fragment>
    )
}

export default Table;