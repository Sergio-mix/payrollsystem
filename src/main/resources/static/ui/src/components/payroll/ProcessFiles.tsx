import {Fragment, useEffect, useState} from "react";
import {
    AiOutlineBulb,
    AiOutlineCheck,
    AiOutlineClose,
    AiOutlineFileExcel,
    GrSecure
} from "react-icons/all";
import {savePayroll} from "../../services/payrollService";
import {useNavigate} from "react-router-dom";
import {AiFillLock, AiOutlineCaretLeft} from "react-icons/ai";

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

        const handleClick200 = () => {
            props.modal.open(<div>
                <AiOutlineCheck className={"text-color-green font-size-60"}/>
                <p className={"font-size-25 text-color-grey"}>{response.data}</p>
            </div>);
        }

        const handleClick401 = () => {
            props.modal.openIsCloseNot(<div>
                <p className={"font-size-30 text-color-grey"}><AiFillLock/> Expired Session</p>
            </div>);

            setTimeout(() => {
                localStorage.clear();
                navigate("/login");
            }, 2000);
        }

        const handleClick400 = () => {
            let datainconsistencies = 0;

            if (response.data.payrollFileData !== null) {
                response.data.payrollFileData.map(item => {
                    datainconsistencies += item.validateErrors.length
                });
            }

            props.modal.open(<div className={"mb-3"}>
                <AiOutlineBulb className={"text-color-yellow font-size-60"}/>
                <p className={"font-size-20 text-color-grey"}>There were inconsistencies in the information in the
                    file</p>
                <ul>
                    <li>File format inconsistencies: {response.data.validateErrors.length} </li>
                    <li>file data inconsistencies: {datainconsistencies} </li>
                </ul>
            </div>);
            console.log(response.data);
        }

        const handleClick500 = () => {
            props.modal.open(<div>
                <AiOutlineClose className={"text-color-red font-size-60"}/>
                <p className={"font-size-25 text-color-grey"}>{response.data}</p>
            </div>);
        }

        // @ts-ignore
        const query = (file) => {
            savePayroll(file).then(response => {
                setResponse(response);
                props.cont();
            }).catch(error => {
                setResponse(error.response);
                props.cont();
            });
        };

        useEffect(() => {
            return () => {
                query(props.file);
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
                    <div className={"font-size-30"}>{
                        response.status !== null ?
                            response.status === 200
                                ? <AiOutlineCheck onClick={handleClick200}
                                                  className={"text-color-green cursor-pointer"}/>
                                : response.status === 400
                                    ? <AiOutlineBulb onClick={handleClick400}
                                                     className={"text-color-yellow cursor-pointer"}/>
                                    : response.status === 500
                                        ? <AiOutlineClose onClick={handleClick500}
                                                          className={"text-color-red cursor-pointer"}/>
                                        : response.status === 401
                                            ? <AiFillLock onClick={handleClick401}
                                                          className={"text-color-grey cursor-pointer"}/>
                                            : <AiOutlineClose onClick={handleClick500}
                                                              className={"text-color-red cursor-pointer"}/>
                            : <span className="spinner-border font-size-14 text-color-grey"/>}
                    </div>
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
                </div>
                <div className={"mt-4"}>
                    <span onClick={() => {
                        contNum === props.list.length ? props.exit() : props.modal.open(<div>
                            <h1>❕</h1>
                            <p className={"font-size-25 text-color-grey"}>Processing files</p>
                        </div>);
                    }}
                          className={"font-size-18 text-color-grey-hover-aux cursor-pointer"}>
                <AiOutlineCaretLeft/> back</span>
                </div>
            </div>
        </div>
    )
}
export default ProcessFiles;