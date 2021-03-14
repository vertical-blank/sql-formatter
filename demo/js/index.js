const ref = require('ref-napi');
const ffi = require('ffi-napi');

const version = '2.0.0'

const libJava = ffi.Library(__dirname + '/sqlformatterdemo', {
  graal_create_isolate: [
    ref.types.int, [
      ref.refType(ref.types.void),
      ref.refType(ref.types.void),
      ref.refType(ref.refType(ref.types.void))
    ]],
  graal_tear_down_isolate: [
    ref.types.int, [
      ref.refType(ref.types.void)]
  ],
  hello: [
    ref.types.CString,
    [ref.refType(ref.types.void)]],
  format_sql: [
    ref.types.CString, [
      ref.refType(ref.types.void),
      ref.refType(ref.types.CString)
    ]],
})

/**
 * HTTP Cloud Function.
 *
 * @param {Object} req Cloud Function request context.
 * @param {Object} res Cloud Function response context.
 */
exports.handler = (req, res) => {
  const p_graal_isolatethread_t = ref.alloc(ref.refType(ref.types.void))

  const rc = libJava.graal_create_isolate(ref.NULL, ref.NULL, p_graal_isolatethread_t)
  if (rc !== 0) {
    res.header('Access-Control-Allow-Origin', "*");
    res.header('Access-Control-Allow-Headers', "Origin, X-Requested-With, Content-Type, Accept");
    res.send('error on isolate creation or attach')
    return;
  }
  if (req.method !== 'POST') {
    const hello = libJava.hello(ref.deref(p_graal_isolatethread_t))
    res.header('Access-Control-Allow-Origin', "*");
    res.header('Access-Control-Allow-Headers', "Origin, X-Requested-With, Content-Type, Accept");
    res.send(`${hello} ${version}`);
  } else {
    const formatted = libJava.format_sql(ref.deref(p_graal_isolatethread_t), ref.allocCString(req.rawBody.toString()))
    res.header('Access-Control-Allow-Origin', "*");
    res.header('Access-Control-Allow-Headers', "Origin, X-Requested-With, Content-Type, Accept");
    res.send(formatted);
  }

  libJava.graal_tear_down_isolate(ref.deref(p_graal_isolatethread_t));
};
