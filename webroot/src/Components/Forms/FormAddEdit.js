import React from 'react';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';

class AddEditForm extends React.Component {
  state = {
    name: '',
    url: '',
  }

  onChange = e => {
    this.setState({[e.target.name]: e.target.value})
  }

  submitFormAdd = e => {
    e.preventDefault()
    fetch('/service', {
      method: 'post',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: this.state.name,
        url: this.state.url,
      })
    })
      .then(response => response.json())
      .then(this.props.toggle())
      .then(item => {
      	this.props.addItemToState(item[0])
      	this.props.toggle()
      })
      .catch(err => console.log(err))
  }

  submitFormEdit = e => {
    e.preventDefault()
    fetch('/service', {
      method: 'put',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: this.state.name,
        url: this.state.url,
      })
    })
      .then(response => response.json())
      .then(this.props.toggle())
      .then(item => {
      	console.log(item)
          this.props.updateState(item[0])
          this.props.toggle()
      })
      .catch(err => console.log(err))
  }

  componentDidMount(){
    // if item exists, populate the state with proper data
    if(this.props.item){
      const { id, status, name, url, added} = this.props.item
      this.setState({ id, status, name, url, added })
    }
  }

  render() {
    return (
      <Form onSubmit={this.props.item ? this.submitFormEdit : this.submitFormAdd}>
        <FormGroup>
          <Label for="name">Name</Label>
          <Input type="text" name="name" id="name" onChange={this.onChange} value={this.state.name === null ? '' : this.state.name}  />
        </FormGroup>
        <FormGroup>
          <Label for="email">URL</Label>
          <Input type="text" name="url" id="url" onChange={this.onChange} value={this.state.url === null ? '' : this.state.url}  />
        </FormGroup>
        <Button>Submit</Button>
      </Form>
    );
  }
}

export default AddEditForm
