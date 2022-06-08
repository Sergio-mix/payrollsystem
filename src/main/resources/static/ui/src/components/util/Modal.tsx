import {Fragment} from "react";

const Modal = (props) => {

    return (
        <Fragment>
            <div className={"modal-container " + (props.show === true ? "show-modal" : " ")}>
                <div className="modal-v1 border-radius-main box-shadow-main">
                    {props.content}
                    {(props.isClose === undefined || props.isClose === true ?
                        <button className={"btn-close-modal box-shadow-main-2 w-50 border-radius-main"}
                                onClick={props.close}>Close</button> : null)}
                </div>
            </div>
        </Fragment>
    )
}

export default Modal;