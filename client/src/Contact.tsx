import * as React from 'react';

class Contact extends React.Component {
  public render() {
    return (
      <div className="App">
        <h1>CONTACT</h1>
        <div>
          <p>出演依頼等はこちらのメールアドレスへお願いします。</p>
          <p><a href="mailto:info@junk-tion.jp">info@junk-tion.jp</a></p>
        </div>
      </div>
    );
  }
}

export default Contact;
