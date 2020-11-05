import React, { Component } from 'react'
import { Table, Button } from 'reactstrap';
import ModalForm from '../Modals/Modal'

class DataTable extends Component {

  deleteItem = id => {
    let confirmDelete = window.confirm('Delete item?')
    if(confirmDelete){
      fetch('/service/' + id, {
      method: 'delete',
      headers: {
        'Content-Type': 'application/json'
      },
    })
      .then(response => response.json())
      .then(id => {
        this.props.deleteItemFromState(id)
        console.log(id)
      })
      .catch(err => console.log(err))
    }

  }

  render() {

    const items = this.props.items.map(item => {
      return (
        <tr key={item.name}>
          <td>{item.status}</td>
          <td scope="row">{item.name}</td>
          <td>{item.url}</td>
          <td>{item.added}</td>
          <td>
            <div style={{width:"110px"}}>
              <ModalForm buttonLabel="Edit" item={item} updateState={this.props.updateState}/>
              {' '}
              <Button color="danger" onClick={() => this.deleteItem(item.name)}>Del</Button>
            </div>
          </td>
        </tr>
        )
      })

    return (
      <Table responsive hover>
        <thead>
          <tr>
            <th>status</th>
            <th>name</th>
            <th>url</th>
            <th>added</th>
          </tr>
        </thead>
        <tbody>
          {items}
        </tbody>
      </Table>
    )
  }
}

export default DataTable
