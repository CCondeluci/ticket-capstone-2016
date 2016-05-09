<?php

class TransactionController extends AppController
{
  var $name = 'Transaction';
  var $scaffold;


  public function view($id = null) {
    if(!$id) {
      throw new NotFoundException(__('Invalid post'));
    }

    $t = $this ->Transaction->find('all',
      array('conditions' => array('Transaction.ObjectId' => $id)));
    if(!$t) {
      throw new NotFoundException(__('Invalid post'));
    }
    $this->set('Transaction', $t);
  }


}
