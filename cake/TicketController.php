<?php

class TicketController extends AppController
{
  var $name = 'Ticket';
  var $scaffold;

  function index()
  {
    $this->paginate = array(
      'limit' => 9,
      'order' => array(
        'Transactions.submitted' => 'DESC'

      ));
    $Tickets = $this->paginate('Ticket');
    $this->set(compact('Tickets'));
  }

  public function view($id = null)
  {
    if (!$id)
    {
      throw new NotFoundException(__('Invalid post'));
    }

    $t = $this->Ticket->findById($id);
    if (!$t)
    {
      throw new NotFoundException(__('Invalid post'));
    }
    $this->set('Ticket', $t);
  }


}
