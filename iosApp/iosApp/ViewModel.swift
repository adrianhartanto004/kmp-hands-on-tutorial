import Foundation
import shared

class ViewModel: ObservableObject {
  let sdk: SpaceXSDK
  @Published var launches = LoadableLaunches.loading

  init(sdk: SpaceXSDK) {
    self.sdk = sdk
    self.loadLaunches(forceReload: false)
  }

  func loadLaunches(forceReload: Bool) {
    self.launches = .loading
    sdk.getLaunches(forceReload: forceReload, completionHandler: { launches, error in
      if let launches = launches {
        self.launches = .result(launches)
      } else {
        self.launches = .error(error?.localizedDescription ?? "error")
      }
    })
  }
}

enum LoadableLaunches {
  case loading
  case result([RocketLaunch])
  case error(String)
}
