<?php

class AttachmentController extends AppController
{
  var $name = 'Attachment';
  var $scaffold;


  public function view($id = null)
  {
    if (!$id)
    {
      throw new NotFoundException(__('Invalid post'));
    }

    $a = $this->Attachment->find('all',
      array('conditions' => array('Attachment.TransactionId' => $id)));
    if (!$a)
    {
      throw new NotFoundException(__('Invalid post'));
    }
    $this->set('Attachment', $a);
  }


}
