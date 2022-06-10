import {Fragment, useEffect, useState} from "react";
import {
    AiOutlineBulb,
    AiOutlineCheck,
    AiOutlineClose,
    AiOutlineFileExcel
} from "react-icons/all";
import {savePayroll} from "../../services/payrollService";
import {useNavigate} from "react-router-dom";

const ProcessFiles = (props) => {
    const navigate = useNavigate();
    const [contNum, setContNum] = useState(0);
    const [items, setItems] = useState(null);
    let num = 0;

    function setCont() {
        num++;
        setContNum(num);
    }

    function Item(props) {
        const [response, setResponse] = useState({data: null, status: null});

        const size = (bytes) => {
            let i = -1;
            let byteUnits = [' kB', ' MB', ' GB', ' TB', 'PB', 'EB', 'ZB', 'YB'];
            do {
                bytes = bytes / 1024;
                i++;
            }
            while (bytes > 1024);
            return Math.max(bytes, 0.1).toFixed(1) + byteUnits[i];
        }

        const handleClick = () => {
            // props.modal.open(<div>
            //     <p className={"font-size-25 text-color-grey"}>{response.data}</p>
            // </div>);
            console.log(response.data);
        }

        // @ts-ignore
        const query = (file) => {
            savePayroll(file).then(response => {
                setResponse(response);
            }).catch(error => {
                setResponse({data: error.response.data, status: error.response.status});
            });
        };

        useEffect(() => {
            return () => {
                query(props.file);
                props.cont();
            }
        }, []);


        return (
            <Fragment>
                <div
                    className={"d-flex center justify-content-between border-radius-main box-shadow-hover mt-3 ps-2 pe-2 w-100"}>
                    <div className={"center"}>
                        <AiOutlineFileExcel className={"text-color-green font-size-35"}/>
                        <div className={"mt-2 ms-2"}>
                            <bdi className={"font-size-16"}>{props.file.name}</bdi>
                            <p className={"font-size-14 text-color-grey "}>{size(props.file.size)}</p>
                        </div>
                    </div>
                    <div className={"font-size-30"}>{response.status !== null ? response.status === 200 ?
                            <AiOutlineCheck onClick={handleClick}
                                            className={"text-color-green cursor-pointer"}/> : response.status === 400 ?
                                <AiOutlineBulb onClick={handleClick}
                                               className={"text-color-yellow cursor-pointer"}/> : response.status === 500 ?
                                    <AiOutlineClose onClick={handleClick} className={"text-color-red cursor-pointer"}/> :
                                    <AiOutlineClose className={"text-color-red cursor-pointer"}/> :
                        <span className="spinner-border font-size-14 text-color-grey"/>}</div>
                </div>
            </Fragment>
        )
    }

    const listItems = (list) => {
        if (list.length > 0) {
            return <div className={"form-table div-content-scroll mh-350"}>
                {list.map((item, index) => (
                    <Item key={index} file={item} cont={setCont} modal={props.modal}/>
                ))}
            </div>
        } else {
            return null
        }
    }

    useEffect(() => {
        return () => {
            setItems(listItems(props.list));
        }
    }, []);

    return (
        <div className={"form-main form-first box-shadow-main border-radius-main mb-5 effect-main"}>
            <h1 className={"text-center"}>Process payroll</h1>
            <div className={"row"}>
                <p className={"font-size-18 text-color-grey mb-2 text-center"}>We are processing payroll please
                    don't
                    leave the page</p>
                {items}
                <div className={"mt-5 text-center"}>
                    <span
                        className={"font-size-18 text-color-grey "}>Processed {contNum} files of {props.list.length}</span>
                    <div className={"mt-4"}>
                        <span className={"font-size-18 text-color-grey-hover-red"} onClick={() => {
                            contNum === props.list.length ? props.exit() : null
                        }}>Exit</span>
                    </div>
                </div>
            </div>
        </div>
    )
}
export default ProcessFiles;