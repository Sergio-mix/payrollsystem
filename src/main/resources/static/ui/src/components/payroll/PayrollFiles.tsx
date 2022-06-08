import {Fragment, useEffect, useState} from "react";
import AddFiles from "./AddFiles";
import ProcessFiles from "./ProcessFiles";

const PayrollFiles = (props) => {
    const [fileList, setFileList] = useState([]);
    const [component, setComponent] = useState(null);

    const FORM_1 = "FORM_1";
    const FORM_2 = "FORM_2";

    const setContent = (component) => {
        switch (component) {
            case FORM_1:
                return <AddFiles list={fileList} addList={setFileList} event={handleChange}/>;
            case FORM_2:
                return <ProcessFiles list={fileList} modal={props.modal}/>;
        }
    }

    const handleChange = () => {
        setComponent(FORM_2);
    }

    useEffect(() => {
        document.title = "PayrollFile | PayrollFile";
        setComponent(FORM_1);
    }, []);

    return (
        <Fragment>
            {setContent(component)}
        </Fragment>
    )
}
export default PayrollFiles;