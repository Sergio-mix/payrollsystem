import {Fragment, useEffect, useRef, useState} from "react";
import {AiOutlineCloudUpload, AiOutlineDelete, AiOutlineFileExcel} from "react-icons/all";

const AddFiles = (props) => {
    const wrapperRef = useRef(null);
    const onDragEnter = () => wrapperRef.current.classList.add('dragover');

    const onDragLeave = () => wrapperRef.current.classList.remove('dragover');

    const onDrop = () => wrapperRef.current.classList.remove('dragover');

    const onFileDrop = (e) => {
        const newFile = e.target.files;
        let updatedList = [...props.list];
        if (newFile) {
            for (let file of newFile) {
                if (file.name.endsWith(".xlsx")
                    && !containsName(updatedList, file.name)) {
                    updatedList.push(file);
                }
            }
        }
        if (props.list.length !== updatedList.length) {
            props.addList(updatedList);
        }
        e.target.value = null;
    }

    const containsName = (list, name) => {
        for (let file of list) {
            if (file.name === name) {
                return true;
            }
        }
        return false;
    };

    const fileRemove = (file) => {
        const updatedList = [...props.list];
        updatedList.splice(props.list.indexOf(file), 1);
        props.addList(updatedList);
    }

    const listItems = (list) => {
        if (list.length > 0) {
            return <div className={"form-div-content-2 div-content-scroll mh-250"}>
                {list.map((item, index) => (
                    <Item key={index} file={item} remove={fileRemove}/>
                ))}
            </div>
        } else {
            return null
        }
    }

    function Item(data) {
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

        return (
            <Fragment>
                <div
                    className={"d-flex center justify-content-between border-radius-main box-shadow-hover mt-3 ps-2 pe-2 w-100"}>
                    <div className={"center"}>
                        <AiOutlineFileExcel className={"text-color-green font-size-35"}/>
                        <div className={"mt-4 ms-2"}>
                            <bdi className={"font-size-16"}>{data.file.name}</bdi>
                            <p className={"font-size-14 text-color-grey "}>{size(data.file.size)}</p>
                        </div>
                    </div>
                    <AiOutlineDelete onClick={() => data.remove(data.file)}
                                     className={"font-size-25 cursor-pointer text-color-grey-hover-red"}/>
                </div>
            </Fragment>
        )
    }


    const next = () => {
        if(props.list.length > 0){
            props.event();
        }else {
            props.modal.open(<div>
                <h1>❕</h1>
                <p className={"font-size-25 text-color-grey"}>There are no files at this time</p>
            </div>);
        }
    };

    useEffect(() => {
        document.title = "Add file | Payroll";
    }, []);

    return (
        <div className={"form-main form-table box-shadow-main border-radius-main mb-5 effect-main"}>
            <h1>Add payroll</h1>
            <div className={"row"}>
                <p className={"font-size-18 text-color-grey"}>Enter the files to process them, remember that only
                    the
                    xlsx format is
                    accepted</p>
                <div className={"form-input"}>
                    <div
                        ref={wrapperRef}
                        className="drop-file-input text-center"
                        onDragEnter={onDragEnter}
                        onDragLeave={onDragLeave}
                        onDrop={onDrop}>
                        <div className="">
                            <AiOutlineCloudUpload className={"font-size-80 text-color-aux"}/>
                            <p>Drag & Drop your files here</p>
                        </div>
                        <input className={""} type={"file"} accept=".xlsx" multiple
                               onChange={onFileDrop} required/>
                    </div>
                </div>
                {listItems(props.list)}
                <div className={"d-flex justify-content-between mt-5"}>
                    <h1 className={"mt-1 text-color-grey font-size-20"}>
                        Files: {props.list.length}
                    </h1>

                    <button className={"w-20 btn-3-user bg-aux-color border-radius-main-2 box-shadow-main-2"}
                            onClick={next}>Process
                    </button>
                </div>
            </div>
        </div>
    )
};

export default AddFiles;